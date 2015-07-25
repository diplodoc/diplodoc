package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.GroovyBindings
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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Component
class GroovyBindingsImpl implements GroovyBindings {

    List<GroovyBindingEnhancer> executionEnhancers
    List<GroovyBindingEnhancer> selfStartingEnhancers
    List<GroovyBindingEnhancer> isListeningToEnhancers
    List<GroovyBindingEnhancer> isWaitingForEnhancers

    @Autowired
    RestTemplate restTemplate

    @Autowired
    ProcessInteractor processInteractor

    @PostConstruct
    void init() {
        executionEnhancers = [
            new InputParametersExecutionEnhancer(),
            new InputExecutionEnhancer(),
            new GetExecutionEnhancer(restTemplate: restTemplate),
            new PostExecutionEnhancer(restTemplate: restTemplate),
            new SendExecutionEnhancer(processInteractor: processInteractor),
            new OutputExecutionEnhancer(processInteractor: processInteractor),
            new EmitExecutionEnhancer(processInteractor: processInteractor),
            new ListenExecutionEnhancer(),
            new WaitingExecutionEnhancer(),
            new StartExecutionEnhancer(processInteractor: processInteractor)
        ]

        selfStartingEnhancers = [
            new SelfStartingEnhancer()
        ]

        isListeningToEnhancers = [
            new IsListeningToEnhancer()
        ]

        isWaitingForEnhancers = [
            new IsWaitingForEnhancer()
        ]
    }

    @Override
    Binding executionBinding(Process process, Map input, ProcessRun processRun) {
        enhance(executionEnhancers, [ process: process, input: input, processRun: processRun ])
    }

    @Override
    Binding selfStartingBinding(Process process) {
        enhance(selfStartingEnhancers, [ process: process ])
    }

    @Override
    Binding isListeningToBinding(Process source, Process destination) {
        enhance(isListeningToEnhancers, [ source: source, destination: destination ])
    }

    @Override
    Binding isWaitingForBinding(String event, Process destination) {
        enhance(isWaitingForEnhancers, [ event: event, destination: destination ])
    }

    Binding enhance(List<GroovyBindingEnhancer> enhancers, Map context) {
        Binding binding = new Binding()

        enhancers.each { enhancer -> binding = enhancer.enhance(binding, context) }

        return binding
    }
}
