package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRepository
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRunRepository
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 * test
 */
class DiploexecSpecs extends Specification {

    ThreadPoolTaskExecutor threadPool = Mock(ThreadPoolTaskExecutor)
    ProcessRepository processRepository = Mock(ProcessRepository)
    ProcessRunRepository processRunRepository = Mock(ProcessRunRepository)

    Diploexec diploexec = Spy(Diploexec)

    def 'void init()'() {
        setup:
            Process[] processes = [ new Process(name: 'process-1'), new Process(name: 'process-2') ]
            processRepository.findByActiveIsTrue() >> processes
            diploexec.processRepository = processRepository

            diploexec.findEventsOneWaitsFor({ it.name == 'process-1' }) >> [ 'event-1-for-process-1', 'event-2-for-process-1' ]
            diploexec.findEventsOneWaitsFor({ it.name == 'process-2' }) >> [ 'event-3-for-process-2', 'event-4-for-process-2' ]

            diploexec.findProcessesOneListensTo({ it.name == 'process-1' }) >> [ 'process-2', 'process-3' ]
            diploexec.findProcessesOneListensTo({ it.name == 'process-2' }) >> [ 'process-1', 'process-4' ]

        when:
            diploexec.init()

        then:
            diploexec.waitsForEventsMap.size() == 2
            diploexec.waitsForEventsMap[new Process(name: 'process-1')] == [ 'event-1-for-process-1', 'event-2-for-process-1' ]
            diploexec.waitsForEventsMap[new Process(name: 'process-2')] == [ 'event-3-for-process-2', 'event-4-for-process-2' ]

            diploexec.listenToProcessesMap.size() == 2
            diploexec.listenToProcessesMap[new Process(name: 'process-1')] == [ 'process-2', 'process-3' ]
            diploexec.listenToProcessesMap[new Process(name: 'process-2')] == [ 'process-1', 'process-4' ]
    }

    def 'void run(ProcessRun processRun)'() {
        setup:
            ProcessRun processRun = new ProcessRun()

            diploexec.threadPool = threadPool


        when:
            diploexec.run(processRun)

        then:
            1 * threadPool.execute({ ProcessCall processCall ->
                processCall.diploexec == diploexec && processCall.processRun == processRun
            })
    }

    def 'void notify(DiploexecEvent event)'() {
        setup:
            ProcessRun[] processRuns = [ new ProcessRun(), new ProcessRun() ]
            DiploexecEvent event = Mock(DiploexecEvent)

            event.notifiedRuns(_) >> processRuns
            diploexec.threadPool = threadPool

        when:
            diploexec.notify(event)

        then:
            1 * diploexec.run(processRuns[0])
            1 * diploexec.run(processRuns[1])
    }

    def 'void notify(ProcessCallEvent event) - process run started event'() {
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

    def 'void notify(ProcessCallEvent event) - process run succeed event'() {
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

    def 'void notify(ProcessCallEvent event) - process run failed event'() {
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

    def 'Process getProcess(String name)'() {
        setup:
            diploexec.processes = [ new Process(name: 'process-0'), new Process(name: 'process-1'), new Process(name: 'process-2') ]

        when:
            Process actual = diploexec.getProcess('process-1')

        then:
            actual == new Process(name: 'process-1')
    }

    def 'Collection<Process> getProcessesWaitingFor(String eventName)'() {
        setup:
            Process process1 = new Process(name: 'process-1')
            Process process2 = new Process(name: 'process-2')
            diploexec.waitsForEventsMap = [:]
            diploexec.waitsForEventsMap.put(process1, [ 'event-1', 'event-2' ])
            diploexec.waitsForEventsMap.put(process2, [ 'event-2', 'event-3' ])

        when:
            Collection<Process> actual = diploexec.getProcessesWaitingFor('event-1')

        then:
            actual.size() == 1
            actual[0] == process1
    }

    def 'Collection<Process> getProcessesListeningTo(Process outputProcess)'() {
        setup:
            Process process1 = new Process(name: 'process-1')
            Process process2 = new Process(name: 'process-2')
            diploexec.listenToProcessesMap = [:]
            diploexec.listenToProcessesMap.put(process1, [ 'process-1', 'process-2' ])
            diploexec.listenToProcessesMap.put(process2, [ 'process-2', 'process-3' ])

        when:
            Collection<Process> actual = diploexec.getProcessesListeningTo(new Process(name: 'process-1'))

        then:
            actual.size() == 1
            actual[0] == process1
    }

    def 'Collection<String> findEventsOneWaitsFor(Process process)'() {
        setup:
            Process process0 = new Process(name: 'process-0')
            process0.definition = "command\nwaiting for: 'process-1'\ncommand\nlisten to: 'process-2'\ncommand"

        when:
            Collection<Process> actual = diploexec.findEventsOneWaitsFor(process0)

        then:
            actual.size() == 1
            actual[0] == 'process-1'
    }

    def 'Collection<String> findProcessesOneListensTo(Process process)'() {
        setup:
            Process process0 = new Process(name: 'process-0')
            process0.definition = "command\nwaiting for: 'process-1'\ncommand\nlisten to: 'process-2'\ncommand"

        when:
            Collection<Process> actual = diploexec.findProcessesOneListensTo(process0)

        then:
            actual.size() == 1
            actual[0] == 'process-2'
    }
}
