package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class OutputEventSpec extends Specification {

    def 'Collection<ProcessRun> shouldNotifyRuns(Diploexec diploexec)'() {
        setup:
            Process process0 = new Process(name: 'process-0')

            Diploexec diploexec = Mock(Diploexec)
            diploexec.getProcessesListeningTo(process0) >> [ new Process(name: 'process-1'), new Process(name: 'process-2') ]

            OutputEvent outputEvent = new OutputEvent(new ProcessRun(process: process0), [ 'key' : 'value' ])

        when:
            Collection<ProcessRun> actual = outputEvent.shouldNotifyRuns(diploexec)

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

    def 'boolean equals(Object other)'() {
        expect:
            new OutputEvent(new ProcessRun(id: 1), [ 'key' : 'value' ]).equals(other) == expected

        where:
            other                                                                            | expected
            new OutputEvent(new ProcessRun(id: 1), [ 'key' : 'value' ])                      | true
            new OutputEvent(new ProcessRun(id: 2), [ 'key' : 'value' ])                      | false
            new OutputEvent(new ProcessRun(id: 1), [ 'key-2' : 'value-2' ])                  | false
            new OutputEvent(new ProcessRun(id: 1), [:])                                      | false
            new OutputEvent(new ProcessRun(id: 1), [ 'key' : 'value', 'key-2' : 'value-2' ]) | false
            new OutputEvent(null, [:])                                                       | false
            new OutputEvent(new ProcessRun(id: 1), null)                                     | false
    }
}
