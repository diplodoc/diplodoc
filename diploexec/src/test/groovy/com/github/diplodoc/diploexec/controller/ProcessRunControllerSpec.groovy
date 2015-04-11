package com.github.diplodoc.diploexec.controller

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRunParameter
import com.github.diplodoc.diploexec.Diploexec
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessRunControllerSpec extends Specification {

    Diploexec diploexec = Mock(Diploexec)

    ProcessRunController processRunController = new ProcessRunController(diploexec: diploexec)

    def 'void run(ProcessRun processRun)'() {
        when:
            ProcessRun processRun = new ProcessRun(id : 1)
            processRun.parameters = [
                new ProcessRunParameter(key: 'key-1', value: 'value-1'),
                new ProcessRunParameter(key: 'key-2', value: 'value-2')
            ]

            processRunController.run(processRun)

        then:
            1 * diploexec.run({ arg ->
                arg.id == 1 &&
                arg.parameters == [
                    new ProcessRunParameter(key: 'key-1', value: 'value-1', processRun: arg),
                    new ProcessRunParameter(key: 'key-2', value: 'value-2', processRun: arg)
                ]
            })
    }
}
