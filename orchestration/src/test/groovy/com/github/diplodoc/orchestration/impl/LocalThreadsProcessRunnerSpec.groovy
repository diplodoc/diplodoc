package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.ProcessRunManager
import org.bson.types.ObjectId
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class LocalThreadsProcessRunnerSpec extends Specification {

    private final static ObjectId ID_1 = new ObjectId('111111111111111111111111')
    private final static ObjectId ID_2 = new ObjectId('222222222222222222222222')

    ProcessInteractor processInteractor = Mock(ProcessInteractor)
    ProcessRunManager processRunManager = Mock(ProcessRunManager)
    ThreadPoolTaskScheduler threadPoolTaskScheduler = Mock(ThreadPoolTaskScheduler)
    GroovyBindings groovyBindings = Mock(GroovyBindings)
    LocalThreadsProcessRunner localThreadsProcessRunner = new LocalThreadsProcessRunner(processInteractor: processInteractor, processRunManager: processRunManager, scheduler: threadPoolTaskScheduler, groovyBindings: groovyBindings)

    def 'Collection<ProcessRun> selfStart()'() {
        given:
            Collection<ProcessRun> expected = [ new ProcessRun(id: ID_1), new ProcessRun(id: ID_2) ]

        when:
            1 * processInteractor.processSelfStart() >> expected

            def actual = localThreadsProcessRunner.selfStart()

        then:
            actual == expected
    }

    def 'ProcessRun start(Process process, Map parameters)'() {
        given:
            Process process = new Process(id: ID_1)
            Map parameters = [ key: 'value' ]
            ProcessRun processRun = new ProcessRun(id: ID_2)

        when:
            1 * processRunManager.create(process, parameters) >> processRun
            1 * threadPoolTaskScheduler.execute({ RunnableProcess runnableProcess ->
                runnableProcess.processRun == processRun &&
                runnableProcess.process == process &&
                runnableProcess.parameters == parameters &&
                runnableProcess.groovyBindings == groovyBindings &&
                runnableProcess.processRunManager == processRunManager
            })

            def actual = localThreadsProcessRunner.start(process, parameters)

        then:
            actual == processRun
    }

    def 'ProcessRun start(Process process)'() {
        given:
            Process process = new Process(id: ID_1)
            ProcessRun processRun = new ProcessRun(id: ID_2)

        when:
            1 * processRunManager.create(process, [:]) >> processRun
            1 * threadPoolTaskScheduler.execute({ RunnableProcess runnableProcess ->
                runnableProcess.processRun == processRun &&
                runnableProcess.process == process &&
                runnableProcess.parameters == [:] &&
                runnableProcess.groovyBindings == groovyBindings &&
                runnableProcess.processRunManager == processRunManager
            })

            def actual = localThreadsProcessRunner.start(process)

        then:
            actual == processRun
    }

    def 'ProcessRun schedule(Process process, Date startAt)'() {
        given:
            Process process = new Process(id: ID_1)
            Date date = new Date()
            ProcessRun processRun = new ProcessRun(id: ID_2)

        when:
            1 * processRunManager.create(process, [:]) >> processRun
            1 * threadPoolTaskScheduler.schedule({ RunnableProcess runnableProcess ->
                runnableProcess.processRun == processRun &&
                runnableProcess.process == process &&
                runnableProcess.parameters == [:] &&
                runnableProcess.groovyBindings == groovyBindings &&
                runnableProcess.processRunManager == processRunManager
            }, date)

            def actual = localThreadsProcessRunner.schedule(process, date)

        then:
            actual == processRun
    }
}
