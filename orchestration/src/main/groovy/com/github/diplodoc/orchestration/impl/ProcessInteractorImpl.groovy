package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.ProcessRunner

/**
 * @author yaroslav.yermilov
 */
class ProcessInteractorImpl implements ProcessInteractor {

    ProcessRunner processRunner

    ProcessRepository processRepository

    GroovyBindings groovyBindings

    @Override
    Collection<Process> selfStartingProcesses() {
        processRepository.findByActiveIsTrue().findAll this.&isSelfStarting
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

    private boolean isSelfStarting(Process process) {
        String selfStartingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('start') }).first()

        new GroovyShell(groovyBindings.selfStartingBinding(process)).evaluate(selfStartingDefinition)
    }
}
