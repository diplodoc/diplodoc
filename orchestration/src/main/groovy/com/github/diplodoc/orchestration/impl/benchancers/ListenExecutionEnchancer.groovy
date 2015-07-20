package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class ListenExecutionEnchancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.listen = { /* do nothing */ }
        return binding
    }
}
