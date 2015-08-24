package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer
import com.github.diplodoc.orchestration.ProcessInteractor

import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
class StartExecutionEnhancer implements GroovyBindingEnhancer {

    ProcessInteractor processInteractor

    @Override
    Binding enhance(Binding binding, Map context) {
        binding.start = { Map params ->
            long period = params.remove 'every'
            if (period > 0) {
                processInteractor.repeatOnce(context.process, period)
            }
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
