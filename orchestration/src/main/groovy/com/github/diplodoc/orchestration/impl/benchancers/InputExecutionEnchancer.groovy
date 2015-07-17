package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class InputExecutionEnchancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun) {
        binding.input = { String[] args ->
            args.each { arg ->
                if (!binding.hasVariable(arg)) {
                    throw new RuntimeException("Input parameter ${arg} is missing")
                }
            }
        }

        return binding
    }
}
