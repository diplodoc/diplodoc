package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class OutputEventSpec extends Specification {

    def 'Collection<ProcessRun> shouldNotifyRuns(Orchestrator orchestrator)'() {
        setup:
            Process process0 = new Process(id: new ObjectId('000000000000000000000000'))

            Orchestrator orchestrator = Mock(Orchestrator)
            orchestrator.getProcess(new ObjectId('000000000000000000000000')) >> process0
            orchestrator.getProcessesListeningTo(process0) >> [ new Process(id: new ObjectId('111111111111111111111111')), new Process(id: new ObjectId('222222222222222222222222')) ]

            OutputEvent outputEvent = new OutputEvent(new ProcessRun(processId: new ObjectId('000000000000000000000000')), [ 'key' : 'value' ])

        when:
            Collection<ProcessRun> actual = outputEvent.shouldNotifyRuns(orchestrator)

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
            new OutputEvent(new ProcessRun(id: new ObjectId('111111111111111111111111')), [ 'key' : 'value' ]).equals(other) == expected

        where:
            other                                                                                                                   | expected
            new OutputEvent(new ProcessRun(id: new ObjectId('111111111111111111111111')), [ 'key' : 'value' ])                      | true
            new OutputEvent(new ProcessRun(id: new ObjectId('222222222222222222222222')), [ 'key' : 'value' ])                      | false
            new OutputEvent(new ProcessRun(id: new ObjectId('111111111111111111111111')), [ 'key-2' : 'value-2' ])                  | false
            new OutputEvent(new ProcessRun(id: new ObjectId('111111111111111111111111')), [:])                                      | false
            new OutputEvent(new ProcessRun(id: new ObjectId('111111111111111111111111')), [ 'key' : 'value', 'key-2' : 'value-2' ]) | false
            new OutputEvent(null, [:])                                                                                              | false
            new OutputEvent(new ProcessRun(id: new ObjectId('111111111111111111111111')), null)                                     | false
    }
}
