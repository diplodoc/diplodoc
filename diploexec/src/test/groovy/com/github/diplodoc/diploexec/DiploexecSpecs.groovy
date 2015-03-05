package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRunRepository
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.util.concurrent.ListenableFuture
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
class DiploexecSpecs extends Specification {

    ThreadPoolTaskExecutor threadPool = Mock(ThreadPoolTaskExecutor)
    ProcessRunRepository processRunRepository = Mock(ProcessRunRepository)
    Diploexec diploexec = Spy(Diploexec)

    def 'notify process run by diploexec events'() {
        setup:
            ProcessRun[] processRuns = [ new ProcessRun(), new ProcessRun() ]
            DiploexecEvent event = Mock(DiploexecEvent)

            event.notifiedRuns(_) >> processRuns
            threadPool.submitListenable(_) >> Mock(ListenableFuture)
            diploexec.threadPool = threadPool

        when:
            diploexec.notify(event)

        then:
            1 * diploexec.run(processRuns[0])
            1 * diploexec.run(processRuns[1])
    }

    def 'notify by process call events: process run started'() {
        setup:
            ProcessCallEvent processCallEvent = new ProcessCallEvent()
            processCallEvent.type = ProcessCallEvent.Type.PROCESS_RUN_STARTED
            processCallEvent.processRun = new ProcessRun()
            processCallEvent.time = LocalDateTime.now()

            diploexec.processRunRepository = processRunRepository

        when:
            diploexec.notify(processCallEvent)

        then:
            1 * processRunRepository.save(processCallEvent.processRun)

        expect:
            processCallEvent.processRun.exitStatus == 'NOT FINISHED'
            processCallEvent.processRun.startTime == processCallEvent.time.toString()
    }

    def 'notify by process call events: process run succeed'() {
        setup:
            ProcessCallEvent processCallEvent = new ProcessCallEvent()
            processCallEvent.type = ProcessCallEvent.Type.PROCESS_RUN_SUCCEED
            processCallEvent.processRun = new ProcessRun()
            processCallEvent.time = LocalDateTime.now()

            diploexec.processRunRepository = processRunRepository

        when:
            diploexec.notify(processCallEvent)

        then:
            1 * processRunRepository.save(processCallEvent.processRun)

        expect:
            processCallEvent.processRun.exitStatus == 'SUCCEED'
            processCallEvent.processRun.endTime == processCallEvent.time.toString()
    }

    def 'notify by process call events: process run failed'() {
        setup:
            ProcessCallEvent processCallEvent = new ProcessCallEvent()
            processCallEvent.type = ProcessCallEvent.Type.PROCESS_RUN_FAILED
            processCallEvent.processRun = new ProcessRun()
            processCallEvent.time = LocalDateTime.now()

            diploexec.processRunRepository = processRunRepository

        when:
            diploexec.notify(processCallEvent)

        then:
            1 * processRunRepository.save(processCallEvent.processRun)

        expect:
            processCallEvent.processRun.exitStatus == 'FAILED'
            processCallEvent.processRun.endTime == processCallEvent.time.toString()
    }

    def 'get process'() {
        setup:
            diploexec.processes = [ new Process(name: 'process-0'), new Process(name: 'process-1'), new Process(name: 'process-2') ]

        when:
            Process actual = diploexec.getProcess('process-1')

        then:
            actual.name == 'process-1'
    }

    def 'get wait processes'() {
        setup:
            Process process1 = new Process(name: 'process-1')
            Process process2 = new Process(name: 'process-2')
            diploexec.waitingMap = [:]
            diploexec.waitingMap.put(process1, [ 'event-1', 'event-2' ])
            diploexec.waitingMap.put(process2, [ 'event-2', 'event-3' ])

        when:
            Collection<Process> actual = diploexec.getWaitProcesses('event-1')

        then:
            actual.size() == 1
            actual[0] == process1
    }

    def 'get input processes'() {
        setup:
            Process process1 = new Process(name: 'process-1')
            Process process2 = new Process(name: 'process-2')
            diploexec.outputMap = [:]
            diploexec.outputMap.put(process1, [ 'process-1', 'process-2' ])
            diploexec.outputMap.put(process2, [ 'process-2', 'process-3' ])

        when:
            Collection<Process> actual = diploexec.getInputProcesses(new Process(name: 'process-1'))

        then:
            actual.size() == 1
            actual[0] == process1
    }

    def 'waitsFor()'() {
        setup:
            Process process0 = new Process(name: 'process-0')
            process0.definition = '''command
waiting for: 'process-1'
command
listen to: 'process-2'
command
'''

        when:
            Collection<Process> actual = diploexec.waitsFor(process0)

        then:
            actual.size() == 1
            actual[0] == 'process-1'
    }

    def 'inputFor()'() {
        setup:
            Process process0 = new Process(name: 'process-0')
            process0.definition = '''command
waiting for: 'process-1'
command
listen to: 'process-2'
command
'''

        when:
            Collection<Process> actual = diploexec.inputFor(process0)

        then:
            actual.size() == 1
            actual[0] == 'process-2'
    }
}
