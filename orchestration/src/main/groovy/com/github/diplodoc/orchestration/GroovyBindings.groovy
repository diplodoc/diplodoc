package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface GroovyBindings {

    Binding executionBinding(Process process, Map input, ProcessRun processRun)

    Binding selfStartingBinding(Process process)
}