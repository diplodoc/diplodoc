package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.ProcessInteractor
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class EmitExecutionEnhancerSpec extends Specification {

    ProcessInteractor processInteractor = Mock(ProcessInteractor)
    EmitExecutionEnhancer emitExecutionEnhancer = new EmitExecutionEnhancer(processInteractor: processInteractor)

    def 'Binding enhance(Binding binding, Map context) - emit works well'() {
        given:
            Binding binding = emitExecutionEnhancer.enhance(new Binding(), [:])

        when:
            binding.emit.call([ 'that': 'event', 'key': 'value' ])

        then:
            1 * processInteractor.emit('event', [ 'key': 'value' ])
    }
}
