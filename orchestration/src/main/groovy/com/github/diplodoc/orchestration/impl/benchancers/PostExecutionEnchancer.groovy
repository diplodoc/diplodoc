package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class PostExecutionEnchancer implements GroovyBindingEnhancer {

    RestTemplate restTemplate

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.post = this.&post
        return binding
    }

    private def post(Map params) {
        String root = params.root
        String path = params.to
        Object request = params.request
        Class responseType = params.expect ?: String

        String url = "${System.getProperty 'modules_host'}/$root/$path"

        restTemplate.postForObject(url, request, responseType)
    }
}
