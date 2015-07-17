package com.github.diplodoc.orchestration.old

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import groovy.transform.ToString

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@ToString
class ProcessCallEvent {

    enum Type { PROCESS_RUN_STARTED, PROCESS_RUN_SUCCEED, PROCESS_RUN_FAILED }

    Type type
    LocalDateTime time
    ProcessRun processRun

    static ProcessCallEvent started(ProcessRun processRun) {
        ProcessCallEvent event = new ProcessCallEvent()
        event.time = LocalDateTime.now()
        event.type = ProcessCallEvent.Type.PROCESS_RUN_STARTED
        event.processRun = processRun

        return event
    }

    static ProcessCallEvent succeed(ProcessRun processRun) {
        ProcessCallEvent event = new ProcessCallEvent()
        event.time = LocalDateTime.now()
        event.type = ProcessCallEvent.Type.PROCESS_RUN_SUCCEED
        event.processRun = processRun

        return event
    }

    static ProcessCallEvent failed(ProcessRun processRun) {
        ProcessCallEvent event = new ProcessCallEvent()
        event.time = LocalDateTime.now()
        event.type = ProcessCallEvent.Type.PROCESS_RUN_FAILED
        event.processRun = processRun

        return event
    }
}
