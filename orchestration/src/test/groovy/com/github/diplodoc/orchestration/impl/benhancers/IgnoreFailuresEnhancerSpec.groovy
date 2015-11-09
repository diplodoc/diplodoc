package com.github.diplodoc.orchestration.impl.benhancers

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class IgnoreFailuresEnhancerSpec extends Specification {

    IgnoreFailuresEnhancer ignoreFailuresEnhancer = new IgnoreFailuresEnhancer()

    def 'void ignoreFailures(Closure action) - action does not fail'() {
        given:
            Binding binding = ignoreFailuresEnhancer.enhance(new Binding(), [:])
            Closure action = Mock(Closure)

        when:
            binding.ignoreFailures.call(action)

        then:
            1 * action.call()
    }

    def 'void ignoreFailures(Closure action) - action fails'() {
        given:
            Binding binding = ignoreFailuresEnhancer.enhance(new Binding(), [:])
            Closure action = Mock(Closure)

        when:
            binding.ignoreFailures.call(action)

        then:
            1 * action.call() >> { throw new RuntimeException() }
            noExceptionThrown()
    }
}
