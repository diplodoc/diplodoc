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

    def get(Map params) {
        String root = params.root
        String path = params.from
        Class responseType = params.expect ? Class.forName(params.expect) : String

        String url = "${modulesHost()}/$root/$path"

        restTemplate.getForObject(url, responseType)
    }

    String modulesHost() {
        System.getProperty 'modules_host'
    }
}
