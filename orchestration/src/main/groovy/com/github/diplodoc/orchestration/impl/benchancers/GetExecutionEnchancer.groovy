package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class GetExecutionEnchancer implements GroovyBindingEnhancer {

    RestTemplate restTemplate

    @Override
    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun) {
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
