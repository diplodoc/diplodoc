package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.domain.mongodb.orchestration.Process
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class IsListeningToEnhancerSpec extends Specification {

    IsListeningToEnhancer isListeningToEnhancer = new IsListeningToEnhancer()

    def 'Binding enhance(Binding binding, Map context) - actually listening, single entries only'() {
        given:
            Binding binding = new Binding()
            Map context = [ source: new Process(name: 'source-0') ]

            binding = isListeningToEnhancer.enhance(binding, context)

        when:
            binding.listen.call([ to: 'source-1' ])
            binding.listen.call([ to: 'source-0' ])
            binding.listen.call([ to: 'source-2' ])

        then:
            binding._IS_LISTENING_ == true
    }

    def 'Binding enhance(Binding binding, Map context) - actually listening, lists'() {
        given:
            Binding binding = new Binding()
            Map context = [ source: new Process(name: 'source-0') ]

            binding = isListeningToEnhancer.enhance(binding, context)

        when:
            binding.listen.call([ to: [ 'source-1', 'source-2' ] ])
            binding.listen.call([ to: [ 'source-3', 'source-0' ] ])
            binding.listen.call([ to: [ 'source-2', 'source-4' ] ])

        then:
            binding._IS_LISTENING_ == true
    }

    def 'Binding enhance(Binding binding, Map context) - actually not listening, single entries only'() {
        given:
            Binding binding = new Binding()
            Map context = [ source: new Process(name: 'source-0') ]

            binding = isListeningToEnhancer.enhance(binding, context)

        when:
            binding.listen.call([ to: 'source-1' ])
            binding.listen.call([ to: 'source-2' ])

        then:
            binding._IS_LISTENING_ == false
    }

    def 'Binding enhance(Binding binding, Map context) - actually not listening, lists'() {
        given:
            Binding binding = new Binding()
            Map context = [ source: new Process(name: 'source-0') ]

            binding = isListeningToEnhancer.enhance(binding, context)

        when:
            binding.listen.call([ to: [ 'source-1', 'source-2' ] ])
            binding.listen.call([ to: [ 'source-2', 'source-3' ] ])

        then:
            binding._IS_LISTENING_ == false
    }
}
