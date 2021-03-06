package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor

/**
 * @author yaroslav.yermilov
 */
class OutputExecutionEnhancer implements GroovyBindingEnhancer {

    ProcessInteractor processInteractor

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.output = { Map params -> processInteractor.output(context.process, params) }
        return binding
    }
}
