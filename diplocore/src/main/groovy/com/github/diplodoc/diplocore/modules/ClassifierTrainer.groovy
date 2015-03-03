package com.github.diplodoc.diplocore.modules

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Component('classifier-trainer')
@Slf4j
class ClassifierTrainer implements Bindable {

    RestTemplate restTemplate = new RestTemplate()

    @Override
    void bindSelf(Binding binding) {
        binding.trainClassifier = this.&trainClassifier
    }

    void trainClassifier() {
        log.info('going to train classifier...')

        def response = restTemplate.getForObject('http://localhost:5000/train_model', String)

        log.info('response: {}' , response)

        log.debug('training of classifier finished')
    }
}
