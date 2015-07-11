package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class LocalThreadsOrchestrator implements Orchestrator {

    ProcessRepository processRepository

    @PostConstruct
    void init() {
        log.info "initializing orchestrator..."

        activeProcesses()
                .findAll({ Process process -> findSchedulingPeriod(process) != null })
                .each this.&start
    }

    @Override
    ProcessRun start(Process process, Map parameters) {
        assert null : 'not implemented yet'
    }

    @Override
    ProcessRun start(Process process) {
        start(process, [:])
    }

    private Collection<Process> activeProcesses() {
        processRepository.findByActiveIsTrue()
    }
}
