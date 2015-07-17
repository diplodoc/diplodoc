package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface GroovyBindings {

    Binding executionBinding(ProcessRun processRun, Process process, Map input)
}