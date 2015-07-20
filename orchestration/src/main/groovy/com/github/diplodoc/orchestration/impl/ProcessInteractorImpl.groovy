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

    @Override
    void repeatOnce(Process process, long afterMillis) {
        Date startAt = new Date(System.currentTimeMillis() + afterMillis)
        processRunner.schedule(process, startAt)
    }

    private boolean isSelfStarting(Process process) {
        String selfStartingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('start') }).join('\n')
        Binding selfStartingBinding = groovyBindings.selfStartingBinding(process)

        new GroovyShell(selfStartingBinding).evaluate(selfStartingDefinition)

        return selfStartingBinding._IS_SELF_STARTING_
    }

    private boolean isListeningTo(Process source, Process destination) {
        String isListeningToDefinition = destination.definition.readLines().findAll({ String line -> line.startsWith('listen') }).join('\n')
        Binding isListeningToBinding = groovyBindings.isListeningToBinding(source, destination)

        new GroovyShell(isListeningToBinding).evaluate(isListeningToDefinition)

        return isListeningToBinding._IS_LISTENING_
    }

    private boolean isWaitingFor(String event, Process destination) {
        String isWaitingForDefinition = destination.definition.readLines().findAll({ String line -> line.startsWith('waiting') }).join('\n')
        Binding isWaitingForBinding = groovyBindings.isWaitingForBinding(event, destination)

        new GroovyShell(isWaitingForBinding).evaluate(isWaitingForDefinition)

        return isWaitingForBinding._IS_WAITING_FOR_
    }
}
