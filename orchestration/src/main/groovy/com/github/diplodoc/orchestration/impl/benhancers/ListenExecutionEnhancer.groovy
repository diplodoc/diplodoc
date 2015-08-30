package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class ListenExecutionEnhancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.listen = { /* do nothing */ }
        return binding
    }
}
