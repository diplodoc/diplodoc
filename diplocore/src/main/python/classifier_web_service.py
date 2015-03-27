from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.svm import SVC
from sklearn.pipeline import Pipeline
from sklearn import cross_validation

import pickle
import base64

from flask import Flask
from flask import request

from pymongo import MongoClient
from bson.objectid import ObjectId
from bson.dbref import DBRef


app = Flask(__name__)


@app.route("/post-type-classifier/post/<post_id>/classify")
def classify(post_id):
    client = MongoClient()
    db = client['diplodata']

    post = db.post.find_one({"_id": ObjectId(post_id)})

    topic_map = {}
    topic_ref_list = []
    for record in db.topic.find():
        depickled = record['classifier']
        topic = record['label']
        reference = DBRef('topic', record['_id'])
        decoded = base64.b64decode(depickled)
        text_clf = pickle.loads(decoded)
        predicted = text_clf.predict_proba([post['meaningText']])[0]
        topic_map[topic] = predicted[0]
        topic_ref_list.append({ 'topic_id': record['_id'], 'score': predicted[0] })

    post['predicted_topics'] = topic_ref_list
    db.post.update({"_id": post["_id"]}, post)

    return 'RESULT: ' + str(topic_map)


@app.route("/post-type-classifier/train-from-all-posts", methods=['POST', 'GET'])
def train():
    content = request.json
    partial_train = False
    if content is not None and 'partial_train' in content:
        partial_train = True

    client = MongoClient()
    db = client['diplodata']

    train_texts, train_labels = [], []
    for post in db.post.find():
        if 'train_topics' in post.keys():
            train_texts.append(post['meaningText'])
            train_labels.append([db.dereference(x)['label'] for x in post['train_topics']])

    topics = []
    for record in db.topic.find():
        topic = record['label']
        if not partial_train:
            topics.append(topic)
        elif 'classifier' not in record:
            topics.append(topic)

    train_labels = extend_label_list(train_labels)
    (classifiers, scores) = build_classifiers(train_texts, train_labels, topics)

    for i in range(len(classifiers)):
        topic = topics[i]
        classifier = classifiers[i]
        pickled = pickle.dumps(classifier)
        encoded = base64.b64encode(pickled)

        record = db.topic.find_one({'label': topic})
        record['classifier'] = encoded
        db.topic.update({"_id": record["_id"]}, record, upsert=True)

    for i in range(len(topics)):
        print topics[i]
        print scores[i]

    return "YOUR CLASSIFIER IS READY TO USE: " + str(zip(topics, scores))


def extend_label_list(train_labels):
    extended_train_labels = [None]*len(train_labels)
    for i in range(len(train_labels)):
        extended_train_labels[i] = list(set(reduce(lambda acc, v: acc + parent_list(v), train_labels[i], [])))
    return extended_train_labels

def build_classifiers(train_texts, train_labels, topics):
    classifiers, scores = [], []
    for topic in topics:
        adjusted_labels = adjust_labels(train_labels, topic)
        text_clf = Pipeline([('vect', CountVectorizer()), ('tfidf', TfidfTransformer()), ('clf', SVC(probability=True))])
        cv_train_texts, cv_test_texts, cv_train_labels, cv_test_labels = cross_validation.train_test_split(train_texts, adjusted_labels, test_size=0.2, random_state=0)

        if len(set(cv_train_labels)) <= 1:
            cv_train_texts = train_texts
            cv_train_labels = adjusted_labels

        text_clf = text_clf.fit(cv_train_texts, cv_train_labels)
        score = text_clf.score(cv_test_texts, cv_test_labels)
        classifiers.append(text_clf)
        scores.append(score)
    return classifiers, scores


def parent_list(topic):
    parents = [topic]

    client = MongoClient()
    db = client['diplodata']
    record = db.topic.find_one({'label': topic})
    iterator = record
    while 'parent' in iterator.keys():
        iterator = db.dereference(iterator['parent'])
        parents.append(iterator['label'])
    #print parents
    return parents


def adjust_labels(train_labels, topic):
    adjusted_labels = [0]*len(train_labels)
    for i in range(len(train_labels)):
        # 0 means YES, 1 -- NO
        if topic in train_labels[i]:
            adjusted_labels[i] = 0
        else:
            adjusted_labels[i] = 1
    return adjusted_labels


if __name__ == '__main__':
    app.run(debug=True)
