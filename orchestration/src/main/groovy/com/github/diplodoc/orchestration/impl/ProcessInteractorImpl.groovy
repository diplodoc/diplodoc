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
    void processSelfStart() {
        processRepository.findByActiveIsTrue().findAll(this.&isSelfStarting).each(processRunner.&start)
    }

    @Override
    void send(String destination, Map params) {
        Process destinationProcess = processRepository.findByNameAndActiveIsTrue(destination)
        processRunner.start(destinationProcess, params)
    }

    @Override
    void output(Process source, Map params) {
        processRepository.findByActiveIsTrue()
                .findAll({ Process process -> isListeningTo(source, process)})
                .each({ Process process -> processRunner.start(process, params) })
    }

    @Override
    void emit(String event, Map params) {
        processRepository.findByActiveIsTrue()
                .findAll({ Process process -> isWaitingFor(event, process)})
                .each({ Process process -> processRunner.start(process, params) })
    }

    private boolean isSelfStarting(Process process) {
        String selfStartingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('start') }).first()
        Binding selfStartingBinding = groovyBindings.selfStartingBinding(process)

        new GroovyShell(selfStartingBinding).evaluate(selfStartingDefinition)

        return selfStartingBinding._IS_SELF_STARTING_
    }

    private boolean isListeningTo(Process source, Process destination) {
        String isListeningToDefinition = destination.definition.readLines().findAll({ String line -> line.startsWith('listen') }).join('\n')

        new GroovyShell(groovyBindings.isListeningToBinding(isListeningToDefinition)).evaluate(isListeningToDefinition)
        assert null : 'not implemented yet'
    }

    private boolean isWaitingFor(String event, Process destination) {
        assert null : 'not implemented yet'
    }
}
