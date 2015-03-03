def similarity_score(article_dict, user_dict):
    article_dict = set(article_dict)
    user_dict = set(user_dict)
    return 1.0 * len(article_dict.intersection(user_dict)) / len(article_dict.union(user_dict))


# article_dict_set - list of pairs (article, set of tags)
# user_dict - list of tags preferred by user
def rank(article_dict_set, user_dict):
    return sorted(article_dict_set, key=lambda x: similarity_score(x[1], user_dict), reverse=True)


def test():
    print rank([('a1', ['random', 'something']), ('a2', ['it', 'random']), ('a3', ['it', 'programming'])], ['it', 'programming'])
test()