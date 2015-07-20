package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.GroovyBindings
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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Component
class GroovyBindingsImpl implements GroovyBindings {

    private List<GroovyBindingEnhancer> executionEnchancers
    private List<GroovyBindingEnhancer> selfStartingEnchancers
    private List<GroovyBindingEnhancer> isListeningToEnchancers
    private List<GroovyBindingEnhancer> isWaitingForEnchancers

    @Autowired
    RestTemplate restTemplate

    @Autowired
    ProcessInteractor processInteractor

    @PostConstruct
    void init() {
        executionEnchancers = [
            new InputParametersExecutionEnhancer(),
            new InputExecutionEnchancer(),
            new GetExecutionEnchancer(restTemplate: restTemplate),
            new PostExecutionEnchancer(restTemplate: restTemplate),
            new SendExecutionEnchancer(processInteractor: processInteractor),
            new OutputExecutionEnchancer(processInteractor: processInteractor),
            new EmitExecutionEnchancer(processInteractor: processInteractor),
            new ListenExecutionEnchancer(),
            new WaitingExecutionEnchancer(),
            new StartExecutionEnchancer(processInteractor: processInteractor)
        ]

        selfStartingEnchancers = [
            new SelfStartingEnchancer()
        ]

        isListeningToEnchancers = [
            new IsListeningToEnchancher()
        ]

        isWaitingForEnchancers = [
            new IsWaitingForEnchancer()
        ]
    }

    @Override
    Binding executionBinding(Process process, Map input, ProcessRun processRun) {
        enchance(executionEnchancers, [ process: process, input: input, processRun: processRun ])
    }

    @Override
    Binding selfStartingBinding(Process process) {
        enchance(selfStartingEnchancers, [ process: process ])
    }

    @Override
    Binding isListeningToBinding(Process source, Process destination) {
        enchance(isListeningToEnchancers, [ source: source, destination: destination ])
    }

    @Override
    Binding isWaitingForBinding(String event, Process destination) {
        enchance(isWaitingForEnchancers, [ event: event, destination: destination ])
    }

    private Binding enchance(List<GroovyBindingEnhancer> enchancers, Map context) {
        Binding binding = new Binding()

        enchancers.each { enhancer -> binding = enhancer.enhance(binding, context) }

        return binding

    }
}
