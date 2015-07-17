package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class WaitingEnchancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun) {
        binding.waiting = { /* do nothing */ }
        return binding
    }
}
