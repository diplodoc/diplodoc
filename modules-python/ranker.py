from pymongo import MongoClient
from bson.objectid import ObjectId
from bson.dbref import DBRef


def dot_product(map1, map2):
    res = 0.0
    for key in map1.keys():
        if key in map2:
            res += map1[key] * map2[key]

    return res


def similarity_score(user, doc):
    client = MongoClient()
    db = client['diplodata']

    user_preferences = [(db.dereference(ref)['_id'], score) for (ref, score) in user['preferences']]
    user_preferences_map = dict(user_preferences)

    if 'predicted_labels' not in doc.keys():
        return 0.0

    doc_topics = [(db.dereference(ref)['_id'], score) for (ref, score) in doc['predicted_labels']]
    doc_topics_map = dict(doc_topics)

    return dot_product(user_preferences_map, doc_topics_map)


def rank(user_id):
    client = MongoClient()
    db = client['diplodata']
    user = db.user.find_one({'_id': ObjectId(user_id)})
    if 'preferences' not in user.keys():
        # use global ranking
        return
    docs = []
    for doc in db.doc.find({'predicted_labels': {'$exists': True}}):
        reference = DBRef('doc', doc['_id'])
        score = similarity_score(user, doc)
        docs.append((reference, score))

    docs.sort(key=lambda pair: pair[1], reverse=True)
    user['ranked_articles'] = docs
    db.user.update({'_id': user['_id']}, user)

rank('5509242eed2a690b0fe734bf')