package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class NotifyEventSpecs extends Specification {

    def 'Collection<ProcessRun> shouldNotifyRuns(Diploexec diploexec)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            diploexec.getProcessesWaitingFor('event-1') >> [ new Process(name: 'process-1'), new Process(name: 'process-2') ]

            NotifyEvent notifyEvent = new NotifyEvent('event-1', [ 'key' : 'value' ])

        when:
            Collection<ProcessRun> actual = notifyEvent.shouldNotifyRuns(diploexec)

        then:
            actual.size() == 2

            actual[0].process.name == 'process-1'
            actual[0].parameters[0].key == 'key'
            actual[0].parameters[0].value == '"value"'
            actual[0].parameters[0].type == 'java.lang.String'
            actual[0].parameters[0].processRun == actual[0]

            actual[1].process.name == 'process-2'
            actual[1].parameters[0].key == 'key'
            actual[1].parameters[0].value == '"value"'
            actual[1].parameters[0].type == 'java.lang.String'
            actual[1].parameters[0].processRun == actual[1]
    }
}
