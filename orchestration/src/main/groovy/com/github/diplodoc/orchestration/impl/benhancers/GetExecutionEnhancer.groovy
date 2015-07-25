package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class GetExecutionEnhancer implements GroovyBindingEnhancer {

    RestTemplate restTemplate

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.get = this.&get
        return binding
    }

    private def get(Map params) {
        String root = params.root
        String path = params.from
        Class responseType = params.expect ?: String

        String url = "${System.getProperty 'modules_host'}/$root/$path"

        restTemplate.getForObject(url, responseType)
    }
}
