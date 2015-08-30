package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor

/**
 * @author yaroslav.yermilov
 */
class EmitExecutionEnhancer implements GroovyBindingEnhancer {

    ProcessInteractor processInteractor

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.emit = this.&emit
        return binding
    }

    private void emit(Map params) {
        String eventName = params.remove 'that'
        processInteractor.emit(eventName, params)
    }
}
