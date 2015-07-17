package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.old.NotifyEvent
import com.github.diplodoc.orchestration.old.OldOrchestratorImpl
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class NotifyEventSpec extends Specification {

    def 'Collection<ProcessRun> shouldNotifyRuns(Orchestrator orchestrator)'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
        orchestrator.getProcessesWaitingFor('event-1') >> [ new Process(id: new ObjectId('111111111111111111111111')), new Process(id: new ObjectId('222222222222222222222222')) ]

            NotifyEvent notifyEvent = new NotifyEvent('event-1', [ 'key' : 'value' ])

        when:
            Collection<ProcessRun> actual = notifyEvent.shouldNotifyRuns(orchestrator)

        then:
            actual.size() == 2

            actual[0].processId == new ObjectId('111111111111111111111111')
            actual[0].parameters[0].key == 'key'
            actual[0].parameters[0].value == '"value"'
            actual[0].parameters[0].type == 'java.lang.String'

            actual[1].processId == new ObjectId('222222222222222222222222')
            actual[1].parameters[0].key == 'key'
            actual[1].parameters[0].value == '"value"'
            actual[1].parameters[0].type == 'java.lang.String'
    }

    def 'boolean equals(Object other)'() {
        expect:
            new NotifyEvent('process-0', [ 'key' : 'value' ]).equals(other) == expected

        where:
            other                                                                  | expected
            new NotifyEvent('process-0', [ 'key' : 'value' ])                      | true
            new NotifyEvent('process-1', [ 'key' : 'value' ])                      | false
            new NotifyEvent('process-0', [ 'key-2' : 'value-2' ])                  | false
            new NotifyEvent('process-0', [:])                                      | false
            new NotifyEvent('process-0', [ 'key' : 'value', 'key-2' : 'value-2' ]) | false
            new NotifyEvent(null, [:])                                             | false
            new NotifyEvent('process-0', null)                                     | false
    }
}
