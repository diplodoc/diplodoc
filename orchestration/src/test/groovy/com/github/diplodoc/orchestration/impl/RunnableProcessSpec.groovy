package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessRunManager
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class RunnableProcessSpec extends Specification {

    private final static ObjectId ID_1 = new ObjectId('111111111111111111111111')

    ProcessRunManager processRunManager = Mock(ProcessRunManager)

    GroovyBindings groovyBindings = Mock(GroovyBindings)

    RunnableProcess runnableProcess = Spy(RunnableProcess)

    def 'void run()'() {
        setup:
            runnableProcess.processRunManager = processRunManager
            runnableProcess.groovyBindings = groovyBindings

            ProcessRun processRun = new ProcessRun(id: ID_1)
            runnableProcess.processRun = processRun

        when:
            runnableProcess.run()

        then:
            1 * processRunManager.markJustStarted(processRun) >> processRun

        then:
            1 * runnableProcess.execute() >> {}

        then:
            1 * processRunManager.markJustSucceed(processRun) >> processRun
    }

    def 'void run() - failed execute'() {
        setup:
            runnableProcess.processRunManager = processRunManager
            runnableProcess.groovyBindings = groovyBindings

            ProcessRun processRun = new ProcessRun(id: ID_1)
            runnableProcess.processRun = processRun

            Exception exception = new RuntimeException()

        when:
            runnableProcess.run()

        then:
            1 * processRunManager.markJustStarted(processRun) >> processRun

        then:
            1 * runnableProcess.execute() >> { throw exception }

        then:
            1 * processRunManager.markJustFailed(processRun, exception) >> processRun
    }
}
