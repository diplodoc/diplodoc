package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface ProcessInteractor {

    Collection<ProcessRun> processSelfStart()

    void send(String destination, Map params)

    void output(Process source, Map params)

    void emit(String event, Map params)

    void repeatOnce(Process process, long afterMillis)
}