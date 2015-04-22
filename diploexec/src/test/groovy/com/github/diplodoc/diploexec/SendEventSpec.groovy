package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Process
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SendEventSpec extends Specification {

    def 'Collection<ProcessRun> shouldNotifyRuns(Diploexec diploexec)'() {
        setup:
            Process process0 = new Process(id: new ObjectId('000000000000000000000000'), name: 'process-0')

            Diploexec diploexec = Mock(Diploexec)
            diploexec.getProcess('process-0') >> process0

            SendEvent sendEvent = new SendEvent('process-0', [ 'key' : 'value' ])

        when:
            Collection<ProcessRun> actual = sendEvent.shouldNotifyRuns(diploexec)

        then:
            actual.size() == 1

            actual[0].processId == new ObjectId('000000000000000000000000')
            actual[0].parameters[0].key == 'key'
            actual[0].parameters[0].value == '"value"'
            actual[0].parameters[0].type == 'java.lang.String'
    }

    def 'boolean equals(Object other)'() {
        expect:
            new SendEvent('process-0', [ 'key' : 'value' ]).equals(other) == expected

        where:
            other                                                                | expected
            new SendEvent('process-0', [ 'key' : 'value' ])                      | true
            new SendEvent('process-1', [ 'key' : 'value' ])                      | false
            new SendEvent('process-0', [ 'key-2' : 'value-2' ])                  | false
            new SendEvent('process-0', [:])                                      | false
            new SendEvent('process-0', [ 'key' : 'value', 'key-2' : 'value-2' ]) | false
            new SendEvent(null, [:])                                             | false
            new SendEvent('process-0', null)                                     | false
    }
}
