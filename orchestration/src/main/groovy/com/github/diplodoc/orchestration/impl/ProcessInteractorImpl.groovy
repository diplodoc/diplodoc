package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.ProcessRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class ProcessInteractorImpl implements ProcessInteractor {

    @Autowired
    ProcessRunner processRunner

    @Autowired
    ProcessRepository processRepository

    @Autowired
    GroovyBindings groovyBindings

    @Override
    Collection<ProcessRun> processSelfStart() {
        processRepository.findByActiveIsTrue().findAll(this.&isSelfStarting).collect(processRunner.&start)
    }

    @Override
    ProcessRun send(String destination, Map params) {
        Process destinationProcess = processRepository.findOneByNameAndActiveIsTrue(destination)
        processRunner.start(destinationProcess, params)
    }

    @Override
    Collection<ProcessRun> output(Process source, Map params) {
        processRepository.findByActiveIsTrue()
                .findAll({ Process process -> isListeningTo(source, process)})
                .collect({ Process process -> processRunner.start(process, params) })
    }

    @Override
    Collection<ProcessRun> emit(String event, Map params) {
        processRepository.findByActiveIsTrue()
                .findAll({ Process process -> isWaitingFor(event, process)})
                .collect({ Process process -> processRunner.start(process, params) })
    }

    @Override
    ProcessRun repeatOnce(Process process, long afterMillis) {
        processRunner.schedule(process, dateAfterMillis(afterMillis))
    }

    boolean isSelfStarting(Process process) {
        String selfStartingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('start') }).join('\n')
        Binding selfStartingBinding = groovyBindings.selfStartingBinding(process)

        evaluate(selfStartingBinding, selfStartingDefinition)

        return selfStartingBinding._IS_SELF_STARTING_
    }

    boolean isListeningTo(Process source, Process destination) {
        String isListeningToDefinition = destination.definition.readLines().findAll({ String line -> line.startsWith('listen') }).join('\n')
        Binding isListeningToBinding = groovyBindings.isListeningToBinding(source, destination)

        evaluate(isListeningToBinding, isListeningToDefinition)

        return isListeningToBinding._IS_LISTENING_
    }

    boolean isWaitingFor(String event, Process destination) {
        String isWaitingForDefinition = destination.definition.readLines().findAll({ String line -> line.startsWith('waiting') }).join('\n')
        Binding isWaitingForBinding = groovyBindings.isWaitingForBinding(event, destination)

        evaluate(isWaitingForBinding, isWaitingForDefinition)

        return isWaitingForBinding._IS_WAITING_FOR_
    }

    void evaluate(Binding binding, String definition) {
        new GroovyShell(binding).evaluate(definition)
    }

    Date dateAfterMillis(long afterMillis) {
        new Date(System.currentTimeMillis() + afterMillis)
    }
}
