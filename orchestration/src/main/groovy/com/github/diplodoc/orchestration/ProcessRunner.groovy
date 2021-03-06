package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface ProcessRunner {

    Collection<ProcessRun> selfStart()

    ProcessRun start(Process process, Map parameters)

    ProcessRun start(Process process)

    ProcessRun schedule(Process process, Date startAt)
}