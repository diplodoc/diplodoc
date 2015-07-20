package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface GroovyBindings {

    Binding executionBinding(Process process, Map input, ProcessRun processRun)

    Binding selfStartingBinding(Process process)

    Binding isListeningToBinding(Process source, Process destination)

    Binding isWaitingForBinding(String event, Process destination)
}