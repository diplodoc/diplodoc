package com.github.diplodoc.orchestration.controller

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.orchestration.ProcessRunner
import org.bson.types.ObjectId
import spock.lang.Ignore
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class OrchestratorControllerSpec extends Specification {

    ProcessRunner processRunner = Mock(ProcessRunner)

    ProcessRepository processRepository = Mock(ProcessRepository)

    OrchestratorController orchestratorController = new OrchestratorController(processRunner: processRunner, processRepository: processRepository)

    @Ignore
    def 'String run(String processId)'() {
        given:
            Process process = new Process(id: new ObjectId('111111111111111111111111'))
            ProcessRun processRun = new ProcessRun(id: new ObjectId('222222222222222222222222'), processId: new ObjectId('111111111111111111111111'))

        when:
            1 * processRepository.findOne(new ObjectId('111111111111111111111111')) >> process
            1 * processRunner.start(process) >> processRun

            String actual = orchestratorController.run('111111111111111111111111')

        then:
            actual == '222222222222222222222222'
    }
}
