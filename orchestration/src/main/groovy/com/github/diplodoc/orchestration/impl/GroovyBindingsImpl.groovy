package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.impl.benchancers.EmitEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.GetEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.InputEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.InputParametersEnhancer
import com.github.diplodoc.orchestration.impl.benchancers.ListenEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.OutputEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.PostEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.SendEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.StartEnchancer
import com.github.diplodoc.orchestration.impl.benchancers.WaitingEnchancer
import org.springframework.web.client.RestTemplate

import javax.annotation.PostConstruct
import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
class GroovyBindingsImpl implements GroovyBindings {

    private List<GroovyBindingEnhancer> executionEnchancers

    RestTemplate restTemplate

    ProcessInteractor processInteractor

    @PostConstruct
    void init() {
        executionEnchancers = [
            new InputParametersEnhancer(),
            new InputEnchancer(),
            new GetEnchancer(restTemplate: restTemplate),
            new PostEnchancer(restTemplate: restTemplate),
            new SendEnchancer(processInteractor: processInteractor),
            new OutputEnchancer(processInteractor: processInteractor),
            new EmitEnchancer(processInteractor: processInteractor),
            new ListenEnchancer(),
            new WaitingEnchancer(),
            new StartEnchancer()
        ]
    }

    @Override
    Binding executionBinding(Process process, Map input, ProcessRun processRun) {
        Binding binding = new Binding()

        executionEnchancers.each { enhancer ->
            binding = enhancer.enhance(binding, process, input, processRun)
        }

        return binding
    }
}
