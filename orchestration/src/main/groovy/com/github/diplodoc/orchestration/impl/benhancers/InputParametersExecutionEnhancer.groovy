package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class InputParametersExecutionEnhancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
        context.input.each { key, value -> binding."${key}" = value }
        return binding
    }
}