package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class LocalThreadsProcessRunner implements ProcessRunner {

    ProcessInteractor processInteractor

    @PostConstruct
    @Override
    Collection<ProcessRun> selfStart() {
        log.info "initializing process runner..."
        processInteractor.selfStartingProcesses().each this.&start
    }

    @Override
    ProcessRun start(Process process, Map parameters) {
        assert null : 'not implemented yet'
    }

    @Override
    ProcessRun start(Process process) {
        start(process, [:])
    }
}
