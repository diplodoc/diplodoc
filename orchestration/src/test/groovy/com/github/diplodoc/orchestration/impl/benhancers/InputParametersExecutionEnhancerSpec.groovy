package com.github.diplodoc.orchestration.impl.benhancers

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class InputParametersExecutionEnhancerSpec extends Specification {

    InputParametersExecutionEnhancer inputParametersExecutionEnhancer = new InputParametersExecutionEnhancer()

    def 'Binding enhance(Binding binding, Map context)'() {
        given:
            Binding binding = new Binding()
            Map input = [ 'key1': 'value-1', 'key2': 'value-2' ]

        when:
            def actual = inputParametersExecutionEnhancer.enhance(binding, [ input: input ])

        then:
            actual.key1 == 'value-1'
            actual.key2 == 'value-2'
    }
}