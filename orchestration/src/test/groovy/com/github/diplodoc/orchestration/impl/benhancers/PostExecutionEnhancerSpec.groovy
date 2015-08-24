package com.github.diplodoc.orchestration.impl.benhancers

import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostExecutionEnhancerSpec extends Specification {

    RestTemplate restTemplate = Mock(RestTemplate)
    PostExecutionEnhancer postExecutionEnhancer = Spy(PostExecutionEnhancer)

    def 'Binding enhance(Binding binding, Map context)'() {
        given:
            postExecutionEnhancer.restTemplate = restTemplate
            postExecutionEnhancer.modulesHost() >> 'modulesHost'

            Binding binding = postExecutionEnhancer.enhance(new Binding(), [:])

            def returnValue = 'returnValue'

        when:
            def actual = binding.post.call([ 'root': 'rootValue', 'to': 'toValue', 'request': 'request', 'expect': Integer.name, 'key': 'value' ])

        then:
            1 * restTemplate.postForObject('modulesHost/rootValue/toValue', 'request', Integer) >> returnValue
            actual == returnValue
    }

    def 'Binding enhance(Binding binding, Map context) - default expected type'() {
        given:
            postExecutionEnhancer.restTemplate = restTemplate
            postExecutionEnhancer.modulesHost() >> 'modulesHost'

            Binding binding = postExecutionEnhancer.enhance(new Binding(), [:])

            def returnValue = 'returnValue'

        when:
            def actual = binding.post.call([ 'root': 'rootValue', 'to': 'toValue', 'request': 'request', 'key': 'value' ])

        then:
            1 * restTemplate.postForObject('modulesHost/rootValue/toValue', 'request', String) >> returnValue
            actual == returnValue
    }
}
