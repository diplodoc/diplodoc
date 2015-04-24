package com.github.diplodoc.diplocore.services

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Service
@Slf4j
class VkService {

    static int COUNT = 100

    RestTemplate restTemplate = new RestTemplate()

    Collection search(String query) {
        def response = restTemplate.getForObject("https://api.vk.com/method/newsfeed.search?q=${query}&count=${COUNT}", String)
        def posts = new JsonSlurper().parseText(response)['response']

        posts.findAll({ !(it instanceof Integer) })
    }
}
