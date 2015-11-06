package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import groovy.util.logging.Slf4j

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class IgnoreFailuresEnhancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.ignoreFailures = this.&ignoreFailures
        return binding
    }

    void ignoreFailures(Closure action) {
        try {
            action.call()
        } catch (e) {
            log.warn("Process failed, but it was ignored", e)
        }
    }
}
