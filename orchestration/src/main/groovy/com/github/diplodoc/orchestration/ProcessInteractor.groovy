package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process

/**
 * @author yaroslav.yermilov
 */
interface ProcessInteractor {

    Collection<Process> selfStartingProcesses()
}