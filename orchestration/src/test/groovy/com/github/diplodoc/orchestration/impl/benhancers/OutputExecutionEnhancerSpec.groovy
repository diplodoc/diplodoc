package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.orchestration.ProcessInteractor
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class OutputExecutionEnhancerSpec extends Specification {

    private final static ObjectId ID_1 = new ObjectId('111111111111111111111111')

    ProcessInteractor processInteractor = Mock(ProcessInteractor)
    OutputExecutionEnhancer outputExecutionEnhancer = new OutputExecutionEnhancer(processInteractor: processInteractor)

    def 'Binding enhance(Binding binding, Map context)'() {
        given:
            Process process = new Process(id: ID_1)
            Map context = [ process: process ]

            Map params = [ 'key': 'value' ]

            Binding binding = new Binding()
            binding = outputExecutionEnhancer.enhance(binding, context)

        when:
            binding.output.call(params)

        then:
            1 * processInteractor.output(process, params)
    }
}
