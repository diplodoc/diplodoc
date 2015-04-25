package com.github.diplodoc.diplocore.services

import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class TwitterService {

    static int COUNT = 100

    TwitterTemplate twitterTemplate

    Collection<Tweet> search(String query) {
        twitterTemplate.searchOperations().search(query, COUNT).tweets
    }
}
