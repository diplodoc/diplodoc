package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
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
@Component('classifier-trainer')
@Slf4j
class ClassifierTrainer implements Bindable {

    @Autowired
    PostRepository postRepository

    RestTemplate restTemplate = new RestTemplate()

    @Override
    void bindSelf(Binding binding) {
        binding.trainClassifier = this.&trainClassifier
    }

    void trainClassifier() {
        log.info('going to train classifier...')

        List<Post> posts = postRepository.findByTypeIsNotNull()

        def request = JsonOutput.toJson(posts.collect{[text: it.meaningText, label: it.type]})

        restTemplate.postForLocation('localhost:5050/train', new HttpEntity<>(request))

        log.debug('training of classifier finished')
    }
}
