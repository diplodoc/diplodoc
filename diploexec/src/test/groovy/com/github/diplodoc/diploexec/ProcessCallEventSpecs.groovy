package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCallEventSpecs extends Specification {

    def 'started event'() {
        setup:
            ProcessRun processRun = new ProcessRun()

        when:
            ProcessCallEvent actual = ProcessCallEvent.started(processRun)

        then:
            actual.processRun == processRun
            actual.time != null
            actual.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED
    }

    def 'ended event'() {
        setup:
            ProcessRun processRun = new ProcessRun()

        when:
            ProcessCallEvent actual = ProcessCallEvent.ended(processRun)

        then:
            actual.processRun == processRun
            actual.time != null
            actual.type == ProcessCallEvent.Type.PROCESS_RUN_ENDED
    }
}
