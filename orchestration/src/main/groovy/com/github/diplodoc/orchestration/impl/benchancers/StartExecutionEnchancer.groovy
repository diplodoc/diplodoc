package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor

import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
class StartExecutionEnchancer implements GroovyBindingEnhancer {

    ProcessInteractor processInteractor

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.start = { Map params ->
            long period = params.remove 'every'
            processInteractor.repeatOnce(context.process, period)
        }

        Integer.metaClass.propertyMissing = { String name ->
            TimeUnit timeUnit = TimeUnit.valueOf(name.toUpperCase())
            if (timeUnit != null) {
                return timeUnit.toMillis(delegate)
            }
        }

        return binding
    }
}
