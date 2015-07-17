package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.ProcessRunner

/**
 * @author yaroslav.yermilov
 */
class ProcessInteractorImpl implements ProcessInteractor {

    ProcessRunner processRunner

    ProcessRepository processRepository

    @Override
    Collection<Process> selfStartingProcesses() {
        assert null : 'not implemented yet'
    }

    @Override
    void send(String destination, Map params) {
        Process destinationProcess = processRepository.findByNameAndActiveIsTrue(destination)
        processRunner.start(destinationProcess, params)
    }

    @Override
    void output(Process source, Map params) {
        assert null : 'not implemented yet'
    }

    @Override
    void emit(String event, Map params) {
        assert null : 'not implemented yet'
    }
}
