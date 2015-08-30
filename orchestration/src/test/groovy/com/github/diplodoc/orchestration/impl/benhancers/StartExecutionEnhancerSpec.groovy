package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.orchestration.ProcessInteractor
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class StartExecutionEnhancerSpec extends Specification {

    private final static ObjectId ID_1 = new ObjectId('111111111111111111111111')

    ProcessInteractor processInteractor = Mock(ProcessInteractor)
    StartExecutionEnhancer startExecutionEnhancer = new StartExecutionEnhancer(processInteractor: processInteractor)

    def 'Binding enhance(Binding binding, Map context) - non-zero period'() {
        given:
            Process process = new Process(id: ID_1)
            Map context = [ 'process': process ]

            Binding binding = new Binding()
            binding = startExecutionEnhancer.enhance(binding, context)

        when:
            binding.start.call([ 'every': 5.minutes ])

        then:
            1 * processInteractor.repeatOnce(process, 5 * 1000 * 60)
    }

    def 'Binding enhance(Binding binding, Map context) - zero period'() {
        given:
            Process process = new Process(id: ID_1)
            Map context = [ 'process': process ]

            Binding binding = new Binding()
            binding = startExecutionEnhancer.enhance(binding, context)

        when:
            binding.start.call([ 'every': 0.minutes ])

        then:
            0 * processInteractor.repeatOnce(process, _)
    }
}
