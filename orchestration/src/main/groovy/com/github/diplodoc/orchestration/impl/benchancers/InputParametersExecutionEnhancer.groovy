package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class InputParametersExecutionEnhancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun) {
        input.each { key, value -> binding."${key}" = value }
        return binding
    }
}