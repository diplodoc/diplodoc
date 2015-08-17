package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface ProcessInteractor {

    Collection<ProcessRun> processSelfStart()

    ProcessRun send(String destination, Map params)

    Collection<ProcessRun> output(Process source, Map params)

    Collection<ProcessRun> emit(String event, Map params)

    ProcessRun repeatOnce(Process process, long afterMillis)
}