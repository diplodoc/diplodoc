package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
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

    RestTemplate restTemplate = new RestTemplate()

    @Override
    void bindSelf(Binding binding) {
        binding.classify = { Post post -> classify(post) }
    }

    Post classify(Post post) {
        log.info('going to classify post from {}...', post.url)

        def response = restTemplate.getForObject('http://localhost:5000/classify/{id}', String, [id: post.id])

        log.info('response: {}' , response)

        postRepository.findOne(post.id)
    }
}
