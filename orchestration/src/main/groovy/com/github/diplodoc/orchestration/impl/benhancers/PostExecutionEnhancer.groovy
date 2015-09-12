package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class PostExecutionEnhancer implements GroovyBindingEnhancer {

    RestTemplate restTemplate

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.post = this.&post
        return binding
    }

    def post(Map params) {
        String root = params.root
        String path = params.to
        Object request = params.request

        Class responseType = String
        if (params.expect) {
            if (params.expect instanceof Class) {
                responseType = params.expect
            }
            if (params.expect instanceof String) {
                responseType = Class.forName(params.expect)
            }
        }

        String url = "${modulesHost()}/$root/$path"

        restTemplate.postForObject(url, request, responseType)
    }

    String modulesHost() {
        // TODO: bad, bad design
        System.getProperty 'modules_host' ?: 'localhost:8080/modules-java'
    }
}
