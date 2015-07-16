package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import groovy.util.logging.Slf4j
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class LocalThreadsProcessRunner implements ProcessRunner {

    ThreadPoolTaskScheduler scheduler

    ProcessInteractor processInteractor

    ProcessRunManager processRunManager

    GroovyBindings groovyBindings

    @PostConstruct
    @Override
    Collection<ProcessRun> selfStart() {
        log.info "initializing process runner..."
        processInteractor.selfStartingProcesses().collect this.&start
    }

    @Override
    ProcessRun start(Process process, Map parameters) {
        ProcessRun processRun = processRunManager.create(process, parameters)

        log.info "starting process ${processRun}..."
        scheduler.execute toRunableProcess(processRun, process, parameters)

        return processRun
    }

    @Override
    ProcessRun start(Process process) {
        start(process, [:])
    }

    private RunnableProcess toRunableProcess(ProcessRun processRun, Process process, Map parameters) {
        RunnableProcess runnableProcess = new RunnableProcess(processRun: processRun, process: process, parameters: parameters)

        runnableProcess.processRunManager = processRunManager
        runnableProcess.groovyBindings = groovyBindings

        return runnableProcess
    }
}
