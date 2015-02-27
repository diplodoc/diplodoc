from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.linear_model import SGDClassifier
from sklearn.pipeline import Pipeline

import pickle
import base64

from flask import Flask

from pymongo import MongoClient


app = Flask(__name__)


@app.route("/")
def hello():
    return "LET'S GO"


@app.route("/classify/<int:text_id>")
def classify(text_id):
    client = MongoClient()
    db = client['diplodoc']

    depickled = db.classifier.find_one()['base']
    decoded = base64.b64decode(depickled)

    text_clf = pickle.loads(decoded)

    test_texts, test_labels = [], []
    for post in db.test_data_test.find():
        test_texts.append(post['text'])
        test_labels.append(post['label'])

    predicted = text_clf.predict([test_texts[text_id]])

    return 'REAL LABEL: ' + str(test_labels[text_id]) + ' PREDICTED: ' + str(predicted[0])


@app.route("/train_model")
def train():
    client = MongoClient()
    db = client['diplodoc']

    train_texts, train_labels = [], []
    for post in db.test_data_train.find():
        train_texts.append(post['text'])
        train_labels.append(post['label'])

    text_clf = Pipeline([('vect', CountVectorizer()), ('tfidf', TfidfTransformer()),
                     ('clf', SGDClassifier(loss='hinge', penalty='l2', alpha=1e-3, n_iter=5))])
    text_clf = text_clf.fit(train_texts, train_labels)

    pickled = pickle.dumps(text_clf)
    encoded = base64.b64encode(pickled)
    db.classifier.insert({'base': encoded})

    return "YOUR CLASSIFIER IS READY TO USE"


if __name__ == '__main__':
    app.run(debug=True)