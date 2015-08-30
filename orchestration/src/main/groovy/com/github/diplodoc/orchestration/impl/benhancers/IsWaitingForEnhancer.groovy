package com.github.diplodoc.orchestration.impl.benhancers

import com.github.diplodoc.orchestration.GroovyBindingEnhancer

/**
 * @author yaroslav.yermilov
 */
class IsWaitingForEnhancer implements GroovyBindingEnhancer {

    @Override
    Binding enhance(Binding binding, Map context) {
        binding._IS_WAITING_FOR_ = false

        binding.waiting = { Map params ->
            def waitingFor = params.remove 'for'
            binding._IS_WAITING_FOR_ = binding._IS_WAITING_FOR_ || isWaitingMatch(waitingFor, context.event)
        }

        return binding
    }

    private boolean isWaitingMatch(def waitingFor, String event) {
        if (waitingFor instanceof String) {
            return waitingFor == event
        }
        if (waitingFor instanceof Collection) {
            return waitingFor.contains(event)
        }

        return false
    }
}
