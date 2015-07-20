package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process

/**
 * @author yaroslav.yermilov
 */
interface ProcessInteractor {

    void processSelfStart()

    void send(String destination, Map params)

    void output(Process source, Map params)

    void emit(String event, Map params)

    void repeatOnce(Process process, long afterMillis)
}