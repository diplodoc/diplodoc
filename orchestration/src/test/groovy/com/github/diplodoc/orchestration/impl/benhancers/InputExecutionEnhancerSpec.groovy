package com.github.diplodoc.orchestration.impl.benhancers

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class InputExecutionEnhancerSpec extends Specification {

    InputExecutionEnhancer inputExecutionEnhancer = new InputExecutionEnhancer()

    def 'Binding enhance(Binding binding, Map context) - all variables exists'() {
        given:
            Binding binding = new Binding()
            binding.setVariable('key1', 'value-1')
            binding.setVariable('key2', 'value-2')
            binding = inputExecutionEnhancer.enhance(binding, [:])

        when:
            binding.input.call([ 'key1', 'key2' ] as String[])

        then:
            notThrown Throwable
    }

    def 'Binding enhance(Binding binding, Map context) - missing variable'() {
        given:
            Binding binding = new Binding()
            binding.setVariable('key1', 'value-1')
            binding.setVariable('key2', 'value-2')
            binding = inputExecutionEnhancer.enhance(binding, [:])

        when:
            binding.input.call([ 'key1', 'key2', 'missing' ] as String[])

        then:
            def exception = thrown(RuntimeException)
            exception.message == 'Input parameter missing is missing'
    }
}