package com.github.diplodoc.diploexec.controller

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRunParameter
import com.github.diplodoc.diploexec.Diploexec
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessRunControllerSpec extends Specification {

    Diploexec diploexec = Mock(Diploexec)

    ProcessRunController processRunController = new ProcessRunController(diploexec: diploexec)

    def 'void run(ProcessRun processRun)'() {
        when:
            ProcessRun processRun = new ProcessRun(id: new ObjectId('111111111111111111111111'))
            processRun.parameters = [
                new ProcessRunParameter(key: 'key-1', value: 'value-1'),
                new ProcessRunParameter(key: 'key-2', value: 'value-2')
            ]

            processRunController.run(processRun)

        then:
            1 * diploexec.run({ arg ->
                arg.id == new ObjectId('111111111111111111111111') &&
                arg.parameters == [
                    new ProcessRunParameter(key: 'key-1', value: 'value-1'),
                    new ProcessRunParameter(key: 'key-2', value: 'value-2')
                ]
            })
    }
}
