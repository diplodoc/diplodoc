package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRunParameter
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import com.github.diplodoc.orchestration.ProcessRunManager

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
class ProcessRunManagerImpl implements ProcessRunManager {

    ProcessRunRepository processRunRepository

    @Override
    ProcessRun create(Process process, Map parameters) {
        ProcessRun processRun = new ProcessRun(processId: process.id)
        processRun.parameters = parameters.collect ProcessRunParameter.&fromKeyValue

        processRunRepository.save processRun
    }

    @Override
    ProcessRun markJustStarted(ProcessRun processRun) {
        processRun.startTime = LocalDateTime.now()
        processRun.exitStatus = ProcessRun.EXIT_STATUSES.NOT_FINISHED

        processRunRepository.save processRun
    }

    @Override
    ProcessRun markJustSucceed(ProcessRun processRun) {
        processRun.endTime = LocalDateTime.now()
        processRun.exitStatus = ProcessRun.EXIT_STATUSES.SUCCEED

        processRunRepository.save processRun
    }

    @Override
    ProcessRun markJustFailed(ProcessRun processRun, Throwable reason) {
        processRun.endTime = LocalDateTime.now()
        processRun.exitStatus = ProcessRun.EXIT_STATUSES.FAILED
        processRun.errorMessage = reason.message

        processRunRepository.save processRun
    }
}
