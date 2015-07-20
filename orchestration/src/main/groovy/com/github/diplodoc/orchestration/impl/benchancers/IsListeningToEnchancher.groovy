package com.github.diplodoc.orchestration.impl.benchancers

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.orchestration.GroovyBindingEnhancer

import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
class IsListeningToEnchancher implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
        binding._IS_LISTENING_ = false

        binding.listen = { Map params ->
            def listensTo = params.remove 'to'
            binding._IS_LISTENING_ = binding._IS_LISTENING_ || isListeningMatch(listensTo, context.source)
        }

        return binding
    }

    private boolean isListeningMatch(def listensTo, Process source) {
        if (listensTo instanceof String) {
            return listensTo == source.name
        }
        if (listensTo instanceof Collection) {
            return listensTo.contains(source.name)
        }

        return false
    }
}
