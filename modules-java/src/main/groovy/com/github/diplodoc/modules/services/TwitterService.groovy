package com.github.diplodoc.modules.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Service
@PropertySource('classpath:/twitter.properties')
class TwitterService {

    @Autowired
    Environment environment

    static int COUNT = 100

    TwitterTemplate twitterTemplate

    @PostConstruct
    void init() {
        twitterTemplate = new TwitterTemplate(environment.getProperty('twitter.consumerKey'), environment.getProperty('twitter.consumerSecret'))
    }

    Collection<Tweet> search(String query) {
        twitterTemplate.searchOperations().search(query, COUNT).tweets
    }
}
