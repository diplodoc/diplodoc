package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class ListenEnchancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun) {
        binding.listen = { /* do nothing */ }
        return binding
    }
}
