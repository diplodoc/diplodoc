package com.github.diplodoc.orchestration.impl.benhancers

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class WaitingExecutionEnhancerSpec extends Specification {

    WaitingExecutionEnhancer waitingExecutionEnhancer = new WaitingExecutionEnhancer()

    def 'Binding enhance(Binding binding, Map context)'() {
        given:
            Binding binding = new Binding()

        when:
            binding = waitingExecutionEnhancer.enhance(binding, [:])

        then:
            binding.waiting != null
            binding.waiting instanceof Closure
    }
}
