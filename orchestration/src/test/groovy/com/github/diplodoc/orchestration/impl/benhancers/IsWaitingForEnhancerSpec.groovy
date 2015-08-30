package com.github.diplodoc.orchestration.impl.benhancers

import spock.lang.Specification

/**
 * @author yaroslav.yermilovy
 */
class IsWaitingForEnhancerSpec extends Specification {

    IsWaitingForEnhancer isWaitingForEnhancer = new IsWaitingForEnhancer()

    def 'Binding enhance(Binding binding, Map context) - actually waiting, single entries only'() {
        given:
            Binding binding = new Binding()
            Map context = [ event: 'event-0' ]

            binding = isWaitingForEnhancer.enhance(binding, context)

        when:
            binding.waiting.call([ for: 'event-1' ])
            binding.waiting.call([ for: 'event-0' ])
            binding.waiting.call([ for: 'event-2' ])

        then:
            binding._IS_WAITING_FOR_ == true
    }

    def 'Binding enhance(Binding binding, Map context) - actually waiting, lists'() {
        given:
            Binding binding = new Binding()
            Map context = [ event: 'event-0' ]

            binding = isWaitingForEnhancer.enhance(binding, context)

        when:
            binding.waiting.call([ for: [ 'event-1', 'event-2' ] ])
            binding.waiting.call([ for: [ 'event-3', 'event-0' ] ])
            binding.waiting.call([ for: [ 'event-2', 'event-4' ] ])

        then:
            binding._IS_WAITING_FOR_ == true
    }

    def 'Binding enhance(Binding binding, Map context) - actually not waiting, single entries only'() {
        given:
            Binding binding = new Binding()
            Map context = [ event: 'event-0' ]

            binding = isWaitingForEnhancer.enhance(binding, context)

        when:
            binding.waiting.call([ for: 'event-1' ])
            binding.waiting.call([ for: 'event-2' ])

        then:
            binding._IS_WAITING_FOR_ == false
    }

    def 'Binding enhance(Binding binding, Map context) - actually not waiting, lists'() {
        given:
            Binding binding = new Binding()
            Map context = [ event: 'event-0' ]

            binding = isWaitingForEnhancer.enhance(binding, context)

        when:
            binding.waiting.call([ for: [ 'event-1', 'event-2' ] ])
            binding.waiting.call([ for: [ 'event-2', 'event-3' ] ])

        then:
            binding._IS_WAITING_FOR_ == false
    }
}
