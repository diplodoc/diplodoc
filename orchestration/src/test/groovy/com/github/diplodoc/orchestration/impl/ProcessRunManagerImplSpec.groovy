package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import org.bson.types.ObjectId
import spock.lang.Ignore
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessRunManagerImplSpec extends Specification {

    private final static ObjectId ID_1 = new ObjectId('111111111111111111111111')
    private final static ObjectId ID_2 = new ObjectId('222222222222222222222222')

    ProcessRunRepository processRunRepository = Mock(ProcessRunRepository)
    ProcessRunManagerImpl processRunManager = Spy(ProcessRunManagerImpl)

    @Ignore
    def 'ProcessRun create(Process process, Map parameters)'() {
        given:
            processRunManager.processRunRepository = processRunRepository

            Process process = new Process(id: ID_1)
            Map parameters = [ key: 'value' ]
            ProcessRun expected = new ProcessRun(id: ID_2)

        when:
            1 * processRunRepository.save({ ProcessRun processRun ->
                processRun.processId == ID_1 &&
                processRun.parameters.size() == 1 &&
                processRun.parameters[0].key == 'key' &&
                processRun.parameters[0].type == 'java.lang.String' &&
                processRun.parameters[0].value == '"value"'
            }) >> expected

            def actual = processRunManager.create(process, parameters)

        then:
            expected == actual
    }

    @Ignore
    def 'ProcessRun markJustStarted(ProcessRun processRun)'() {
        given:
            processRunManager.processRunRepository = processRunRepository
            processRunManager.now() >> 'now'

            ProcessRun processRun = new ProcessRun(id: ID_1)
            ProcessRun expected = new ProcessRun(id: ID_2)

        when:
            1 * processRunRepository.save({ ProcessRun arg ->
                arg.id == ID_1 &&
                arg.startTime == 'now' &&
                arg.exitStatus == ProcessRun.EXIT_STATUSES.NOT_FINISHED.toString()
            }) >> expected

            def actual = processRunManager.markJustStarted(processRun)

        then:
            expected == actual
    }

    @Ignore
    def 'ProcessRun markJustSucceed(ProcessRun processRun)'() {
        given:
            processRunManager.processRunRepository = processRunRepository
            processRunManager.now() >> 'now'

            ProcessRun processRun = new ProcessRun(id: ID_1)
            ProcessRun expected = new ProcessRun(id: ID_2)

        when:
            1 * processRunRepository.save({ ProcessRun arg ->
                arg.id == ID_1 &&
                arg.endTime == 'now' &&
                arg.exitStatus == ProcessRun.EXIT_STATUSES.SUCCEED.toString()
            }) >> expected

            def actual = processRunManager.markJustSucceed(processRun)

        then:
            expected == actual
    }

    @Ignore
    def 'ProcessRun markJustFailed(ProcessRun processRun, Throwable reason)'() {
        given:
            processRunManager.processRunRepository = processRunRepository
            processRunManager.now() >> 'now'

            ProcessRun processRun = new ProcessRun(id: ID_1)
            ProcessRun expected = new ProcessRun(id: ID_2)

            Throwable reason = new Exception('message')

        when:
            1 * processRunRepository.save({ ProcessRun arg ->
                arg.id == ID_1 &&
                arg.endTime == 'now' &&
                arg.exitStatus == ProcessRun.EXIT_STATUSES.FAILED.toString() &&
                arg.errorMessage == 'message'
            }) >> expected

            def actual = processRunManager.markJustFailed(processRun, reason)

        then:
            expected == actual
    }
}
