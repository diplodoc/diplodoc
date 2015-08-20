package com.github.diplodoc.orchestration.impl.benhancers

import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class GetExecutionEnhancerSpec extends Specification {

    RestTemplate restTemplate = Mock(RestTemplate)
    GetExecutionEnhancer getExecutionEnhancer = Spy(GetExecutionEnhancer)

    def 'Binding enhance(Binding binding, Map context)'() {
        given:
            getExecutionEnhancer.restTemplate = restTemplate
            getExecutionEnhancer.modulesHost() >> 'modulesHost'

            Binding binding = getExecutionEnhancer.enhance(new Binding(), [:])

            def returnValue = 'returnValue'

        when:
            def actual = binding.get.call([ 'root': 'rootValue', 'from': 'fromValue', 'expect': Integer.name, 'key': 'value' ])

        then:
            1 * restTemplate.getForObject('modulesHost/rootValue/fromValue', Integer) >> returnValue
            actual == returnValue
    }

    def 'Binding enhance(Binding binding, Map context) - default expected type'() {
        given:
            getExecutionEnhancer.restTemplate = restTemplate
            getExecutionEnhancer.modulesHost() >> 'modulesHost'

            Binding binding = getExecutionEnhancer.enhance(new Binding(), [:])

            def returnValue = 'returnValue'

        when:
            def actual = binding.get.call([ 'root': 'rootValue', 'from': 'fromValue', 'key': 'value' ])

        then:
            1 * restTemplate.getForObject('modulesHost/rootValue/fromValue', String) >> returnValue
            actual == returnValue
    }
}