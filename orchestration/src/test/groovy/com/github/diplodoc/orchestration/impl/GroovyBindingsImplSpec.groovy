package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.impl.benchancers.EmitExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.GetExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.InputExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.InputParametersExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benchancers.IsListeningToEnchancher
import com.github.diplodoc.orchestration.impl.benchancers.IsWaitingForEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.ListenExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.OutputExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.PostExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.SelfStartingEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.SendExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.StartExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.WaitingExecutionEnchancer
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
            groovyBindings.executionEnchancers.size() == 10
            groovyBindings.executionEnchancers.find({ it instanceof InputParametersExecutionEnhancer }) != null
            groovyBindings.executionEnchancers.find({ it instanceof InputExecutionEnchancer }) != null
            groovyBindings.executionEnchancers.find({ it instanceof GetExecutionEnchancer && it.restTemplate == restTemplate }) != null
            groovyBindings.executionEnchancers.find({ it instanceof PostExecutionEnchancer && it.restTemplate == restTemplate }) != null
            groovyBindings.executionEnchancers.find({ it instanceof SendExecutionEnchancer && it.processInteractor == processInteractor }) != null
            groovyBindings.executionEnchancers.find({ it instanceof OutputExecutionEnchancer && it.processInteractor == processInteractor }) != null
            groovyBindings.executionEnchancers.find({ it instanceof EmitExecutionEnchancer && it.processInteractor == processInteractor }) != null
            groovyBindings.executionEnchancers.find({ it instanceof ListenExecutionEnchancer }) != null
            groovyBindings.executionEnchancers.find({ it instanceof WaitingExecutionEnchancer }) != null
            groovyBindings.executionEnchancers.find({ it instanceof StartExecutionEnchancer && it.processInteractor == processInteractor }) != null

            groovyBindings.selfStartingEnchancers.size() == 1
            groovyBindings.selfStartingEnchancers.find({ it instanceof SelfStartingEnchancer }) != null

            groovyBindings.isListeningToEnchancers.size() == 1
            groovyBindings.isListeningToEnchancers.find({ it instanceof IsListeningToEnchancher }) != null

            groovyBindings.isWaitingForEnchancers.size() == 1
            groovyBindings.isWaitingForEnchancers.find({ it instanceof IsWaitingForEnchancer }) != null
    }

    def 'Binding executionBinding(Process process, Map input, ProcessRun processRun)'() {
        given:
            Process process = Mock(Process)
            Map input = Mock(Map)
            ProcessRun processRun = Mock(ProcessRun)
            List executionEnchancers = Mock(List)

            groovyBindings.executionEnchancers = executionEnchancers

        when:
            groovyBindings.executionBinding(process, input, processRun)

        then:
            1 * groovyBindings.enchance(executionEnchancers, [ process: process, input: input, processRun: processRun ]) >> null
    }

    def 'Binding selfStartingBinding(Process process)'() {
        given:
            Process process = Mock(Process)
            List selfStartingEnchancers = Mock(List)

            groovyBindings.selfStartingEnchancers = selfStartingEnchancers

        when:
            groovyBindings.selfStartingBinding(process)

        then:
            1 * groovyBindings.enchance(selfStartingEnchancers, [ process: process ]) >> null
    }

    def 'Binding isListeningToBinding(Process source, Process destination)'() {
        given:
            Process source = Mock(Process)
            Process destination = Mock(Process)
            List isListeningToEnchancers = Mock(List)

            groovyBindings.isListeningToEnchancers = isListeningToEnchancers

        when:
            groovyBindings.isListeningToBinding(source, destination)

        then:
        1 * groovyBindings.enchance(isListeningToEnchancers, [ source: source, destination: destination ]) >> null
    }

    def 'Binding isWaitingForBinding(String event, Process destination)'() {
        given:
            Process destination = Mock(Process)
            String event = 'event'
            List isWaitingForEnchancers = Mock(List)

            groovyBindings.isWaitingForEnchancers = isWaitingForEnchancers

        when:
            groovyBindings.isWaitingForBinding(event, destination)

        then:
            1 * groovyBindings.enchance(isWaitingForEnchancers, [ event: event, destination: destination ]) >> null
    }

    def 'Binding enchance(List<GroovyBindingEnhancer> enchancers, Map context)'() {
        given:
            GroovyBindingEnhancer enhancer1 = Mock(GroovyBindingEnhancer)
            GroovyBindingEnhancer enhancer2 = Mock(GroovyBindingEnhancer)
            List enchancers = [ enhancer1, enhancer2 ]

            Map context = Mock(Map)

            Binding binding1 = Mock(Binding)
            Binding binding2 = Mock(Binding)

        when:
            def actual = groovyBindings.enchance(enchancers, context)

        then:
            1 * enhancer1.enhance(_, context) >> binding1
            1 * enhancer2.enhance(binding1, context) >> binding2

            actual == binding2
}
}
