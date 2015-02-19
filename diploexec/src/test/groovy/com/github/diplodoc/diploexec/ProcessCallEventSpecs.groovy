package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCallEventSpecs extends Specification {

    def 'static ProcessCallEvent started(ProcessRun processRun)'() {
        setup:
            ProcessRun processRun = new ProcessRun()

        when:
            ProcessCallEvent actual = ProcessCallEvent.started(processRun)

        then:
            actual.processRun == processRun
            actual.time != null
            actual.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED
    }

    def 'static ProcessCallEvent succeed(ProcessRun processRun)'() {
        setup:
            ProcessRun processRun = new ProcessRun()

        when:
            ProcessCallEvent actual = ProcessCallEvent.succeed(processRun)

        then:
            actual.processRun == processRun
            actual.time != null
            actual.type == ProcessCallEvent.Type.PROCESS_RUN_SUCCEED
    }

    def 'static ProcessCallEvent failed(ProcessRun processRun))'() {
        setup:
            ProcessRun processRun = new ProcessRun()

        when:
            ProcessCallEvent actual = ProcessCallEvent.failed(processRun)

        then:
            actual.processRun == processRun
            actual.time != null
            actual.type == ProcessCallEvent.Type.PROCESS_RUN_FAILED
    }
}
