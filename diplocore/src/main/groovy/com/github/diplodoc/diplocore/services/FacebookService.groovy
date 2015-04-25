package com.github.diplodoc.diplocore.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.social.facebook.api.Post
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Service
@PropertySource('classpath:/facebook.properties')
class FacebookService {

    @Autowired
    Environment environment

    FacebookTemplate facebookTemplate

    @PostConstruct
    void init() {
        facebookTemplate = new FacebookTemplate(environment.getProperty('facebook.accessToken'))
    }

    Collection<Post> groupPosts(String groupName) {
        Collection<Post> result = []
        def page = null
        def response

        while (!page) {
            if (page) {
                response = facebookTemplate.feedOperations().getFeed(groupName, page)
            } else {
                response = facebookTemplate.feedOperations().getFeed(groupName)
            }
            result.addAll(response)
            page = response.nextPage
        }

        return result
    }
}
