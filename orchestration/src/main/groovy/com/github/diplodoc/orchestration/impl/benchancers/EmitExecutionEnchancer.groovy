package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor

/**
 * @author yaroslav.yermilov
 */
class EmitExecutionEnchancer implements GroovyBindingEnhancer {

    ProcessInteractor processInteractor

    @Override
    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun) {
        binding.emit = this.&emit
        return binding
    }

    private void emit(Map params) {
        String eventName = params.remove 'that'
        processInteractor.emit(eventName, params)
    }
}
