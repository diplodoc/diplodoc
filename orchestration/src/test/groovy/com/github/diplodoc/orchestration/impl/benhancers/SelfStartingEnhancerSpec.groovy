package com.github.diplodoc.orchestration.impl.benhancers

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SelfStartingEnhancerSpec extends Specification {

    SelfStartingEnhancer selfStartingEnhancer = new SelfStartingEnhancer()

    def 'Binding enhance(Binding binding, Map context) - actually self starting'() {
        given:
            Binding binding = new Binding()
            binding = selfStartingEnhancer.enhance(binding, [:])

        when:
            binding.start.call([every: 0])
            binding.start.call([every: 5.minutes])
            binding.start.call([every: 0])

        then:
            binding._IS_SELF_STARTING_ == true
    }

    def 'Binding enhance(Binding binding, Map context) - zero period, no self starting'() {
        given:
            Binding binding = new Binding()
            binding = selfStartingEnhancer.enhance(binding, [:])

        when:
            binding.start.call([ every: 0 ])
            binding.start.call([ every: 0.minutes ])

        then:
            binding._IS_SELF_STARTING_ == false
    }

    def 'Binding enhance(Binding binding, Map context) - no start calls, no self starting'() {
        given:
            Binding binding = new Binding()

        when:
            binding = selfStartingEnhancer.enhance(binding, [:])

        then:
            binding._IS_SELF_STARTING_ == false
    }
}
