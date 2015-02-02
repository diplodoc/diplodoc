package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SendEventSpecs extends Specification {

    def 'notifiedRuns'() {
        setup:
            Process process0 = new Process(name: 'process-0')

            Diploexec diploexec = Mock(Diploexec)
            diploexec.getProcess('process-0') >> process0

            SendEvent sendEvent = new SendEvent('process-0', [ 'key' : 'value' ])

        when:
            Collection<ProcessRun> actual = sendEvent.notifiedRuns(diploexec)

        then:
            actual.size() == 1

            actual[0].process.name == 'process-0'
            actual[0].parameters[0].key == 'key'
            actual[0].parameters[0].value == '"value"'
            actual[0].parameters[0].type == 'java.lang.String'
            actual[0].parameters[0].processRun == actual[0]
    }
}
