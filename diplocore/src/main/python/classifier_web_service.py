from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.linear_model import SGDClassifier
from sklearn.pipeline import Pipeline

import pickle
import base64

from flask import Flask

from pymongo import MongoClient
from bson.objectid import ObjectId


app = Flask(__name__)



@app.route("/post-type-classifier/post/classify/<post_id>")
def classify(post_id):
    client = MongoClient()
    db = client['diplodata']

    depickled = db.classifier.find_one()['base']
    decoded = base64.b64decode(depickled)
    text_clf = pickle.loads(decoded)

    record = db.post.find_one({"_id": ObjectId(post_id)})
    predicted = text_clf.predict([record['meaningText']])

    db.post.update({"_id": record["_id"]}, {'meaningText': record['meaningText'], 'type': predicted[0]})
    
    return 'PREDICTED: ' + str(predicted[0])


@app.route("/post-type-classifier/train-from-all-posts")
def train():
    client = MongoClient()
    db = client['diplodata']

    train_texts, train_labels = [], []
    for post in db.post.find():
        if 'type' in post.keys():
            train_texts.append(post['meaningText'])
            train_labels.append(post['type'])

    text_clf = Pipeline([('vect', CountVectorizer()), ('tfidf', TfidfTransformer()),
                     ('clf', SGDClassifier(loss='hinge', penalty='l2', alpha=1e-3, n_iter=5))])
    text_clf = text_clf.fit(train_texts, train_labels)

    pickled = pickle.dumps(text_clf)
    encoded = base64.b64encode(pickled)

    if bool(db.classifier.find_one()):
        classifier_id = db.classifier.find_one()["_id"]
        db.classifier.update({"_id": classifier_id}, {'base': encoded})
    else:
        db.classifier.insert({'base': encoded})

    return "YOUR CLASSIFIER IS READY TO USE"


if __name__ == '__main__':
    app.run(debug=True)
