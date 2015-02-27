from pymongo import MongoClient
from sklearn.datasets import fetch_20newsgroups

categories = ['alt.atheism', 'soc.religion.christian', 'comp.graphics', 'sci.med']
twenty_test = fetch_20newsgroups(subset='test', categories=categories, shuffle=True, random_state=42)
twenty_train = fetch_20newsgroups(subset='train', categories=categories, shuffle=True, random_state=42)

client = MongoClient()
db = client['diplodoc']

for i in range(len(twenty_test.data)):
    db.test_data_test.insert({'text': twenty_test.data[i], 'label': twenty_test.target[i]})
for i in range(len(twenty_train.data)):
    db.test_data_train.insert({'text': twenty_train.data[i], 'label': twenty_train.target[i]})