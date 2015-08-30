package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.orchestration.ProcessInteractor
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SendExecutionEnhancerSpec extends Specification {

    private final static ObjectId ID_1 = new ObjectId('111111111111111111111111')

    ProcessInteractor processInteractor = Mock(ProcessInteractor)
    SendExecutionEnhancer sendExecutionEnhancer = new SendExecutionEnhancer(processInteractor: processInteractor)

    def 'Binding enhance(Binding binding, Map context)'() {
        given:
            Map params = [ 'key': 'value', 'to': 'destination' ]

            Binding binding = new Binding()
            binding = sendExecutionEnhancer.enhance(binding, [:])

        when:
            binding.send.call(params)

        then:
            1 * processInteractor.send('destination', [ 'key': 'value' ])
    }
}
