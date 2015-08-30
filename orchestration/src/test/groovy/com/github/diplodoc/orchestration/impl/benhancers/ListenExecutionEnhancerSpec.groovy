package com.github.diplodoc.orchestration.impl.benhancers

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ListenExecutionEnhancerSpec extends Specification {

    ListenExecutionEnhancer listenExecutionEnhancer = new ListenExecutionEnhancer()

    def 'Binding enhance(Binding binding, Map context)'() {
        given:
            Binding binding = new Binding()

        when:
            binding = listenExecutionEnhancer.enhance(binding, [:])

        then:
            binding.listen != null
            binding.listen instanceof Closure
    }
}
