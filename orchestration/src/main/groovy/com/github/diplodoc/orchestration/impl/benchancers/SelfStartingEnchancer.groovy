package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer

import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
class SelfStartingEnchancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun) {
        binding.start = { Map params ->
            def period = params.remove 'every'
            return (period >= 0)
        }

        Integer.metaClass.propertyMissing = {String name ->
            TimeUnit timeUnit = TimeUnit.valueOf(name.toUpperCase())
            if (timeUnit != null) {
                return timeUnit.toMillis(delegate)
            }
        }

        return binding
    }
}
