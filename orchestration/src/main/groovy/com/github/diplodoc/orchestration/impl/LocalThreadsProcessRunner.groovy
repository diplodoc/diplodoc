package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.ProcessRunManager
import com.github.diplodoc.orchestration.ProcessRunner
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Slf4j
@Component
class LocalThreadsProcessRunner implements ProcessRunner {

    @Autowired
    ThreadPoolTaskScheduler scheduler

    @Autowired
    ProcessInteractor processInteractor

    @Autowired
    ProcessRunManager processRunManager

    @Autowired
    GroovyBindings groovyBindings

    @PostConstruct
    @Override
    Collection<ProcessRun> selfStart() {
        log.info "initializing process runner..."
        processInteractor.processSelfStart()
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

    @Override
    ProcessRun schedule(Process process, Date startAt) {
        ProcessRun processRun = processRunManager.create(process, [:])

        log.info "scheduling process ${processRun} to start at ${startAt}..."
        scheduler.schedule(toRunableProcess(processRun, process, [:]), startAt)

        return processRun
    }

    private RunnableProcess toRunableProcess(ProcessRun processRun, Process process, Map parameters) {
        RunnableProcess runnableProcess = new RunnableProcess(processRun: processRun, process: process, parameters: parameters)

        runnableProcess.processRunManager = processRunManager
        runnableProcess.groovyBindings = groovyBindings

        return runnableProcess
    }
}
