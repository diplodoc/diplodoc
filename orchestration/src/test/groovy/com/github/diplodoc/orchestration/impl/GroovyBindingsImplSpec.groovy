package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.impl.benhancers.EmitExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.GetExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.InputExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.InputParametersExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.IsListeningToEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.IsWaitingForEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.ListenExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.OutputExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.PostExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.SelfStartingEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.SendExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.StartExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benhancers.WaitingExecutionEnhancer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class GroovyBindingsImplSpec extends Specification {

    RestTemplate restTemplate = Mock(RestTemplate)
    ProcessInteractor processInteractor = Mock(ProcessInteractor)

    GroovyBindingsImpl groovyBindings = Spy(GroovyBindingsImpl)

    def 'void init()'() {
        setup:
            groovyBindings.restTemplate = restTemplate
            groovyBindings.processInteractor = processInteractor

        when:
            groovyBindings.init()

        then:
            groovyBindings.executionEnhancers.size() == 10
            groovyBindings.executionEnhancers.find({ it instanceof InputParametersExecutionEnhancer }) != null
            groovyBindings.executionEnhancers.find({ it instanceof InputExecutionEnhancer }) != null
            groovyBindings.executionEnhancers.find({ it instanceof GetExecutionEnhancer && it.restTemplate == restTemplate }) != null
            groovyBindings.executionEnhancers.find({ it instanceof PostExecutionEnhancer && it.restTemplate == restTemplate }) != null
            groovyBindings.executionEnhancers.find({ it instanceof SendExecutionEnhancer && it.processInteractor == processInteractor }) != null
            groovyBindings.executionEnhancers.find({ it instanceof OutputExecutionEnhancer && it.processInteractor == processInteractor }) != null
            groovyBindings.executionEnhancers.find({ it instanceof EmitExecutionEnhancer && it.processInteractor == processInteractor }) != null
            groovyBindings.executionEnhancers.find({ it instanceof ListenExecutionEnhancer }) != null
            groovyBindings.executionEnhancers.find({ it instanceof WaitingExecutionEnhancer }) != null
            groovyBindings.executionEnhancers.find({ it instanceof StartExecutionEnhancer && it.processInteractor == processInteractor }) != null

            groovyBindings.selfStartingEnhancers.size() == 1
            groovyBindings.selfStartingEnhancers.find({ it instanceof SelfStartingEnhancer }) != null

            groovyBindings.isListeningToEnhancers.size() == 1
            groovyBindings.isListeningToEnhancers.find({ it instanceof IsListeningToEnhancer }) != null

            groovyBindings.isWaitingForEnhancers.size() == 1
            groovyBindings.isWaitingForEnhancers.find({ it instanceof IsWaitingForEnhancer }) != null
    }

    def 'Binding executionBinding(Process process, Map input, ProcessRun processRun)'() {
        given:
            Process process = Mock(Process)
            Map input = Mock(Map)
            ProcessRun processRun = Mock(ProcessRun)
            List executionEnhancers = Mock(List)

            groovyBindings.executionEnhancers = executionEnhancers

        when:
            groovyBindings.executionBinding(process, input, processRun)

        then:
            1 * groovyBindings.enhance(executionEnhancers, [ process: process, input: input, processRun: processRun ]) >> null
    }

    def 'Binding selfStartingBinding(Process process)'() {
        given:
            Process process = Mock(Process)
            List selfStartingEnhancers = Mock(List)

            groovyBindings.selfStartingEnhancers = selfStartingEnhancers

        when:
            groovyBindings.selfStartingBinding(process)

        then:
            1 * groovyBindings.enhance(selfStartingEnhancers, [ process: process ]) >> null
    }

    def 'Binding isListeningToBinding(Process source, Process destination)'() {
        given:
            Process source = Mock(Process)
            Process destination = Mock(Process)
            List isListeningToEnhancers = Mock(List)

            groovyBindings.isListeningToEnhancers = isListeningToEnhancers

        when:
            groovyBindings.isListeningToBinding(source, destination)

        then:
            1 * groovyBindings.enhance(isListeningToEnhancers, [ source: source, destination: destination ]) >> null
    }

    def 'Binding isWaitingForBinding(String event, Process destination)'() {
        given:
            Process destination = Mock(Process)
            String event = 'event'
            List isWaitingForEnhancers = Mock(List)

            groovyBindings.isWaitingForEnhancers = isWaitingForEnhancers

        when:
            groovyBindings.isWaitingForBinding(event, destination)

        then:
            1 * groovyBindings.enhance(isWaitingForEnhancers, [ event: event, destination: destination ]) >> null
    }

    def 'Binding enhance(List<GroovyBindingEnhancer> enhancers, Map context)'() {
        given:
            GroovyBindingEnhancer enhancer1 = Mock(GroovyBindingEnhancer)
            GroovyBindingEnhancer enhancer2 = Mock(GroovyBindingEnhancer)
            List enhancers = [ enhancer1, enhancer2 ]

            Map context = Mock(Map)

            Binding binding1 = Mock(Binding)
            Binding binding2 = Mock(Binding)

        when:
            def actual = groovyBindings.enhance(enhancers, context)

        then:
            1 * enhancer1.enhance(_, context) >> binding1
            1 * enhancer2.enhance(binding1, context) >> binding2

            actual == binding2
}
}
