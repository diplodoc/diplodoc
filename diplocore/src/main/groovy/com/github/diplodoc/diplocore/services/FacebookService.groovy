package com.github.diplodoc.diplocore.services

import org.springframework.social.facebook.api.Post
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class FacebookService {

    FacebookTemplate facebookTemplate

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
