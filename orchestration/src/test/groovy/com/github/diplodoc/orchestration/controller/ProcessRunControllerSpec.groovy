package com.github.diplodoc.orchestration.controller

import com.github.diplodoc.orchestration.old.OldOrchestratorImpl
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessRunControllerSpec extends Specification {

    OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)

    OrchestratorController processRunController = new OrchestratorController(orchestrator: orchestrator)

    def 'void run(ProcessRun processRun)'() {
        when:
            processRunController.run('111111111111111111111111')

        then:
            1 * orchestrator.run(new ObjectId('111111111111111111111111'), [])
    }
}
