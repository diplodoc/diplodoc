package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process

/**
 * @author yaroslav.yermilov
 */
interface ProcessInteractor {

    Collection<Process> selfStartingProcesses()

    void send(String destination, Map params)

    void output(Process source, Map params)

    void emit(String event, Map params)
}