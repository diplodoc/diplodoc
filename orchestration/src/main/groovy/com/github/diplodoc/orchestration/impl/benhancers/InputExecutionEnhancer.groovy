package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class InputExecutionEnhancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
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
