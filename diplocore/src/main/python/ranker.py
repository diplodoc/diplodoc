from pymongo import MongoClient
from bson.objectid import ObjectId
from bson.dbref import DBRef


def dot_product(map1, map2):
    res = 0.0
    for key in map1.keys():
        if key in map2:
            res += map1[key] * map2[key]

    return res


def similarity_score(user, post):
    client = MongoClient()
    db = client['diplodata']

    user_preferences = [(db.dereference(ref)['_id'], score) for (ref, score) in user['preferences']]
    user_preferences_map = dict(user_preferences)

    if 'predicted_labels' not in post.keys():
        return 0.0

    post_topics = [(db.dereference(ref)['_id'], score) for (ref, score) in post['predicted_labels']]
    post_topics_map = dict(post_topics)

    return dot_product(user_preferences_map, post_topics_map)


def rank(user_id):
    client = MongoClient()
    db = client['diplodata']
    user = db.user.find_one({'_id': ObjectId(user_id)})
    if 'preferences' not in user.keys():
        # use global ranking
        return
    posts = []
    for post in db.post.find({'predicted_labels': {'$exists': True}}):
        reference = DBRef('post', post['_id'])
        score = similarity_score(user, post)
        posts.append((reference, score))

    posts.sort(key=lambda pair: pair[1], reverse=True)
    user['ranked_articles'] = posts
    db.user.update({'_id': user['_id']}, user)

rank('5509242eed2a690b0fe734bf')