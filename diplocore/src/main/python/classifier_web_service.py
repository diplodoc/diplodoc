from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.linear_model import SGDClassifier
from sklearn.pipeline import Pipeline
from sklearn.datasets import fetch_20newsgroups
import numpy as np

from flask import Flask
from flask import request

from pymongo import MongoClient

app = Flask(__name__)

@app.route("/")
def hello():
    return 'lets go'

@app.route("/classify/<int:text_id>")
def classify(text_id):
    text_clf = Pipeline([('vect', CountVectorizer()), ('tfidf', TfidfTransformer()),
                     ('clf', SGDClassifier(loss='hinge', penalty='l2', alpha=1e-3, n_iter=5))])

    categories = ['alt.atheism', 'soc.religion.christian', 'comp.graphics', 'sci.med']
    twenty_test = fetch_20newsgroups(subset='test', categories=categories, shuffle=True, random_state=42)
    twenty_train = fetch_20newsgroups(subset='train', categories=categories, shuffle=True, random_state=42)
    text_clf = text_clf.fit(twenty_train.data, twenty_train.target)
    predicted = text_clf.predict([twenty_test.data[text_id]])
    return "predicted value " + str(predicted[0])

@app.route("/train", methods=['POST'])
def train():
    content = request.json
    return 'response' + str(content)

if __name__ == '__main__':
        app.run(debug=True)