package com.github.diplodoc.diploexec.controller

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
            processRunController.run('111111111111111111111111')

        then:
            1 * diploexec.run(new ObjectId('111111111111111111111111'), [])
    }
}
