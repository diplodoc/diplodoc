package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindingEnhancer

import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
class SelfStartingEnchancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
        binding._IS_SELF_STARTING_ = false

        binding.start = { Map params ->
            def period = params.remove 'every'
            binding._IS_SELF_STARTING_ = binding._IS_SELF_STARTING_ || (period >= 0)
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
