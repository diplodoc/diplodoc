package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.impl.benchancers.EmitExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.GetExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.InputExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.InputParametersExecutionEnhancer
import com.github.diplodoc.orchestration.impl.benchancers.ListenExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.OutputExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.PostExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.SelfStartingEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.SendExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.StartExecutionEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.WaitingExecutionEnchancer
import org.springframework.web.client.RestTemplate

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
class GroovyBindingsImpl implements GroovyBindings {

    private List<GroovyBindingEnhancer> executionEnchancers
    private List<GroovyBindingEnhancer> selfStartingEnchancers

    RestTemplate restTemplate

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
            new StartExecutionEnchancer()
        ]

        selfStartingEnchancers = [
            new SelfStartingEnchancer()
        ]
    }

    @Override
    Binding executionBinding(Process process, Map input, ProcessRun processRun) {
        enchance(executionEnchancers, process, input, processRun)
    }

    @Override
    Binding selfStartingBinding(Process process) {
        enchance(selfStartingEnchancers, process, null, null)
    }

    private Binding enchance(List<GroovyBindingEnhancer> enchancers, Process process, Map input, ProcessRun processRun) {
        Binding binding = new Binding()

        enchancers.each { enhancer ->
            binding = enhancer.enhance(binding, process, input, processRun)
        }

        return binding

    }
}
