from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.svm import SVC
from sklearn.pipeline import Pipeline

import pickle
import base64

from flask import Flask
from flask import request

from pymongo import MongoClient
from bson.objectid import ObjectId


app = Flask(__name__)


@app.route("/post-type-classifier/post/<post_id>/classify")
def classify(post_id):
    client = MongoClient()
    db = client['diplodata']

    post = db.post.find_one({"_id": ObjectId(post_id)})

    topic_map = {}
    for record in  db.classifier.find():
        depickled = record['classifier']
        topic = record['topic']
        decoded = base64.b64decode(depickled)
        text_clf = pickle.loads(decoded)
        predicted = text_clf.predict_proba([post['meaningText']])[0]
        topic_map[topic] = predicted

    post["predicted_labels"] = topic_map
    db.post.update({"_id": post["_id"]}, post)
    
    return 'RESULT: ' + str(topic_map)


@app.route("/post-type-classifier/train-from-all-posts", methods=['GET', 'POST'])
def train():
    content = request.json
    partial_train = False
    if 'partial_train' in content:
        partial_train = True

    client = MongoClient()
    db = client['diplodata']

    train_texts, train_labels = [], []
    for post in db.post.find():
        if 'labels' in post.keys():
            train_texts.append(post['meaningText'])
            train_labels.append(post['labels'])
    train_texts, train_labels = denormalize_data(train_texts, train_labels)

    topics = []
    for record in db.classifier.find():
        topic = record["topic"]
        if not partial_train:
            topics.append(topic)
        elif "classifier" not in record:
            topics.append(topic)

    classifiers = build_classifiers(train_texts, train_labels, topics)

    for i in range(len(classifiers)):
        topic = topics[i]
        classifier = classifiers[i]
        pickled = pickle.dumps(classifier)
        encoded = base64.b64encode(pickled)

        record = db.classifier.find_one({'topic': topic})
        record['classifier'] = encoded
        db.classifier.update({"_id": record["_id"]}, record, upsert=True)

    return "YOUR CLASSIFIER IS READY TO USE"


def denormalize_data(train_texts, train_labels):
    denormalized_texts, denormalized_labels = [], []
    for i in range(len(train_texts)):
        for label in train_labels[i]:
            denormalized_texts.append(train_texts[i])
            denormalized_labels.append(label)
    return [denormalized_texts, denormalized_labels]


def build_classifiers(train_texts, train_labels, topics):
    classifiers = []
    for topic in topics:
        adjusted_labels = adjust_labels(train_labels, topic)
        text_clf = Pipeline([('vect', CountVectorizer()), ('tfidf', TfidfTransformer()), ('clf', SVC(probability=True))])
        text_clf = text_clf.fit(train_texts, adjusted_labels)
        classifiers.append(text_clf)
    return classifiers


def adjust_labels(train_labels, topic):
    for i in range(len(train_labels)):
        if train_labels[i] == topic:
            train_labels[i] = 0
        else:
            train_labels[i] = 1
    return train_labels


if __name__ == '__main__':
    app.run(debug=True)
