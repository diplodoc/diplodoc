package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Component('post-type-classifier')
@Slf4j
class PostTypeClassifier implements Bindable {

    @Autowired
    PostRepository postRepository

    @Autowired
    RestTemplate restTemplate

    @Override
    void bindSelf(Binding binding) {
        binding.classify = { Post post -> classify(post) }
    }

    Post classify(Post post) {
        log.info('going to classify post from {}...', post.url)

        post = postRepository.findOne(post.id)

        def request = JsonOutput.toJson(text: post.meaningText)

        def response = new JsonSlurper().parseText(restTemplate.exchange('localhost:5050/classify', HttpMethod.POST, new HttpEntity<>(request), String.class))

        post.type = response.label

        post = postRepository.save post

        log.debug('post {} classified as {}', post.url, post.type)
        return post
    }
}
