package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.ProcessRunner
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
@Slf4j
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
        if (processRepository != null) {
            processRepository.findByActiveIsTrue().findAll(this.&isSelfStarting).collect(processRunner.&start)
        } else {
            log.warn "processSelfStart() invoked but processRepository is not initialized. Waiting..."
            new Thread() {

                @Override
                void run() {
                    while (processRepository == null) {
                        log.warn "Still waiting for processRepository to be initialized..."
                        Thread.sleep(TimeUnit.MINUTES.toMillis(1))
                    }
                    log.warn "processRepository is finally initialized."
                    processSelfStart()
                }
            }.start()
        }
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
