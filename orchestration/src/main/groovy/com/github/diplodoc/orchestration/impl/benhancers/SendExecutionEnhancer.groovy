package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor

/**
 * @author yaroslav.yermilov
 */
class SendExecutionEnhancer implements GroovyBindingEnhancer {

    ProcessInteractor processInteractor

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.send = this.&send
        return binding
    }

    private void send(Map params) {
        String destination = params.remove 'to'
        processInteractor.send(destination, params)
    }
}
