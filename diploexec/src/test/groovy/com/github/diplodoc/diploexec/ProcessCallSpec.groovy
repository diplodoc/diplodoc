package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRunParameter
import groovy.json.JsonOutput
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCallSpec extends Specification {

    def 'void run() - successful run'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)

            Process process = new Process(definition: 'definition')
            ProcessRun processRun = new ProcessRun(process: process, parameters: [])
            processRun.parameters << new ProcessRunParameter(key: 'key-1', type: String.name, value: JsonOutput.toJson('someValue'))
            processRun.parameters << new ProcessRunParameter(key: 'key-2', type: Integer.name, value: JsonOutput.toJson(28))

            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])
            1 * processCall.evaluate([ 'key-1': 'someValue', 'key-2': 28 ], 'definition') >> {}
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED })
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_SUCCEED })

        expect:
            processCall.run()
    }

    def 'void run() - failed run'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)

            Process process = new Process(definition: 'definition')
            ProcessRun processRun = new ProcessRun(process: process, parameters: [])
            processRun.parameters << new ProcessRunParameter(key: 'key-1', type: String.name, value: JsonOutput.toJson('someValue'))
            processRun.parameters << new ProcessRunParameter(key: 'key-2', type: Integer.name, value: JsonOutput.toJson(28))

            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])
            1 * processCall.evaluate([ 'key-1': 'someValue', 'key-2': 28 ], 'definition') >> { throw new RuntimeException() }
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED })
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_FAILED })

        expect:
            processCall.run()
    }
}
