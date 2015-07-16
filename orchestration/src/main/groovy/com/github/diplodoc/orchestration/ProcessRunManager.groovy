package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface ProcessRunManager {

    ProcessRun create(Process process, Map parameters)

    ProcessRun markJustStarted(ProcessRun processRun)

    ProcessRun markJustSucceed(ProcessRun processRun)

    ProcessRun markJustFailed(ProcessRun processRun)
}