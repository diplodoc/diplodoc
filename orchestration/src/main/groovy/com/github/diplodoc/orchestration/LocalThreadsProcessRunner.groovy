package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRunParameter
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
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

    ProcessRunRepository processRunRepository

    @PostConstruct
    @Override
    Collection<ProcessRun> selfStart() {
        log.info "initializing process runner..."
        processInteractor.selfStartingProcesses().collect this.&start
    }

    @Override
    ProcessRun start(Process process, Map parameters) {
        ProcessRun processRun = toProcessRun(process, parameters)
        processRunRepository.save processRun

        log.info "starting process ${processRun}..."
        scheduler.execute toRunableProcess(processRun, process, parameters)

        return processRun
    }

    @Override
    ProcessRun start(Process process) {
        start(process, [:])
    }

    private ProcessRun toProcessRun(Process process, Map parameters) {
        ProcessRun processRun = new ProcessRun(processId: process.id)
        processRun.parameters = parameters.collect ProcessRunParameter.&fromKeyValue

        return processRun
    }

    private RunnableProcess toRunableProcess(ProcessRun processRun, Process process, Map parameters) {
        assert null : 'not implemented yet'
    }
}
