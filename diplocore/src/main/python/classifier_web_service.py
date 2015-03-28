from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.svm import SVC
from sklearn.pipeline import Pipeline
import random

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
    print 'start loading data from db...'
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
    print 'finished loading data from db...'

    train_labels = extend_label_list(train_labels)
    print 'extended training labels...'

    print 'start building classifiers...'
    (classifiers, quality_score) = build_classifiers(train_texts, train_labels, topics)

    print 'save perf data...'
    db.stats.insert({'aggregated_score': quality_score})

    print 'save classifiers'
    for i in range(len(classifiers)):
        topic = topics[i]
        classifier = classifiers[i]
        pickled = pickle.dumps(classifier)
        encoded = base64.b64encode(pickled)

        record = db.topic.find_one({'label': topic})
        record['classifier'] = encoded
        db.topic.update({"_id": record["_id"]}, record, upsert=True)

    return "YOUR CLASSIFIER IS READY TO USE: " + str(quality_score)


def extend_label_list(train_labels):
    extended_train_labels = [None]*len(train_labels)
    for i in range(len(train_labels)):
        extended_train_labels[i] = list(set(reduce(lambda acc, v: acc + parent_list(v), train_labels[i], [])))
    return extended_train_labels


def build_classifiers(train_texts, train_labels, topics):
    classifiers = []
    m = len(train_labels)
    zipped_data = zip(train_texts, train_labels)
    random.shuffle(zipped_data)
    shuffled_train_texts, shuffled_train_labels = zip(*zipped_data)
    cv_train_texts, cv_test_texts = shuffled_train_texts[:m/5], shuffled_train_texts[m/5 + 1:]
    cv_train_labels, cv_test_labels = shuffled_train_labels[:m/5], shuffled_train_labels[m/5 + 1:]
    print 'splitted data for cross-validation'

    scores = [None]*len(cv_test_texts)
    for topic in topics:
        cv_train_labels_adjusted = adjust_labels(cv_train_labels, topic)
        text_clf = Pipeline([('vect', CountVectorizer()), ('tfidf', TfidfTransformer()), ('clf', SVC(probability=True))])

        if len(set(cv_train_labels_adjusted)) <= 1:
            cv_train_labels_adjusted = adjust_labels(train_labels, topic)
            text_clf = text_clf.fit(train_texts, cv_train_labels_adjusted)
        else:
            text_clf = text_clf.fit(cv_train_texts, cv_train_labels_adjusted)

        classifiers.append(text_clf)
        print 'classifier for topic %s created' % topic

        for i, test_text in enumerate(cv_test_texts):
            score = text_clf.predict_proba(test_text)[0]
            if scores[i] is None:
                scores[i] = {topic: score}
            else:
                scores[i][topic] = score

    quality_score = final_score(cv_test_labels, scores)

    return classifiers, quality_score


def final_score(test_label_list, predicted_labels_map):
    res_list = []

    for scores_map in predicted_labels_map:
        cur_res = 0.0
        for i, topic in enumerate(scores_map.keys()):
            if topic in test_label_list[i]:
                cur_res += abs(1 - scores_map[topic])
            else:
                cur_res += scores_map[topic]
        res_list.append(cur_res)

    return sum(res_list)/len(res_list)


def parent_list(topic):
    parents = [topic]

    client = MongoClient()
    db = client['diplodata']
    record = db.topic.find_one({'label': topic})
    iterator = record
    while 'parent' in iterator.keys():
        iterator = db.dereference(iterator['parent'])
        parents.append(iterator['label'])

    return parents


def adjust_labels(train_labels, topic):
    adjusted_labels = [0]*len(train_labels)
    for i in range(len(train_labels)):
        if topic in train_labels[i]:
            adjusted_labels[i] = 0
        else:
            adjusted_labels[i] = 1
    return adjusted_labels


if __name__ == '__main__':
    app.run(debug=True)
