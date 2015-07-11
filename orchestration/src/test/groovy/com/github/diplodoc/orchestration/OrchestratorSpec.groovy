package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import org.bson.types.ObjectId
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 * test
 */
class OrchestratorSpec extends Specification {

    ThreadPoolTaskExecutor threadPool = Mock(ThreadPoolTaskExecutor)
    ProcessRepository processRepository = Mock(ProcessRepository)
    ProcessRunRepository processRunRepository = Mock(ProcessRunRepository)

    OldOrchestratorImpl orchestrator = Spy(OldOrchestratorImpl)

    @Ignore
    def 'void run(ProcessRun processRun)'() {
        setup:
            ProcessRun processRun = new ProcessRun(id: new ObjectId('111111111111111111111111'))
            processRunRepository.save(_) >> processRun

            orchestrator.processRunRepository = processRunRepository
            orchestrator.threadPool = threadPool

        when:
            ObjectId actual = orchestrator.run(new ObjectId('111111111111111111111111'), [])

        then:
            actual == new ObjectId('111111111111111111111111')

            1 * threadPool.execute({ ProcessCall processCall ->
                processCall.orchestrator == orchestrator && processCall.processRun == processRun
            })
    }

    @Ignore
    def 'void notify(OrchestrationEvent event)'() {
        setup:
            ProcessRun[] processRuns = [ new ProcessRun(processId: new ObjectId('111111111111111111111111'), parameters: []), new ProcessRun(processId: new ObjectId('222222222222222222222222'), parameters: []) ]
            OrchestrationEvent event = Mock(OrchestrationEvent)

            event.shouldNotifyRuns(_) >> processRuns

            processRunRepository.save(_) >> { it[0] }

            orchestrator.processRunRepository = processRunRepository
            orchestrator.threadPool = threadPool

        when:
            orchestrator.notify(event)

        then:
            1 * orchestrator.run(new ObjectId('111111111111111111111111'), [])
            1 * orchestrator.run(new ObjectId('222222222222222222222222'), [])
    }

    def 'void notify(ProcessCallEvent event) - process run started event'() {
        setup:
            ProcessCallEvent processCallEvent = new ProcessCallEvent()
            processCallEvent.type = ProcessCallEvent.Type.PROCESS_RUN_STARTED
            processCallEvent.processRun = new ProcessRun()
            processCallEvent.time = LocalDateTime.now()

            orchestrator.processRunRepository = processRunRepository

        when:
            orchestrator.notify(processCallEvent)

        then:
            1 * processRunRepository.save(processCallEvent.processRun)

        expect:
            processCallEvent.processRun.exitStatus == 'NOT FINISHED'
            processCallEvent.processRun.startTime == processCallEvent.time.toString()
    }

    @Ignore
    def 'void notify(ProcessCallEvent event) - process run succeed event'() {
        setup:
            ProcessCallEvent processCallEvent = new ProcessCallEvent()
            processCallEvent.type = ProcessCallEvent.Type.PROCESS_RUN_SUCCEED
            processCallEvent.processRun = new ProcessRun()
            processCallEvent.time = LocalDateTime.now()

            orchestrator.processRunRepository = processRunRepository

        when:
            orchestrator.notify(processCallEvent)

        then:
            1 * processRunRepository.save(processCallEvent.processRun)

        expect:
            processCallEvent.processRun.exitStatus == 'SUCCEED'
            processCallEvent.processRun.endTime == processCallEvent.time.toString()
    }

    @Ignore
    def 'void notify(ProcessCallEvent event) - process run failed event'() {
        setup:
            ProcessCallEvent processCallEvent = new ProcessCallEvent()
            processCallEvent.type = ProcessCallEvent.Type.PROCESS_RUN_FAILED
            processCallEvent.processRun = new ProcessRun()
            processCallEvent.time = LocalDateTime.now()

            orchestrator.processRunRepository = processRunRepository

        when:
            orchestrator.notify(processCallEvent)

        then:
            1 * processRunRepository.save(processCallEvent.processRun)

        expect:
            processCallEvent.processRun.exitStatus == 'FAILED'
            processCallEvent.processRun.endTime == processCallEvent.time.toString()
    }

    def 'Process getProcess(String name)'() {
        setup:
            orchestrator.processRepository = processRepository

        when:
            processRepository.findByNameAndActiveIsTrue('process-1') >> [ new Process(id: new ObjectId('111111111111111111111111'), name: 'process-1') ]
            Process actual = orchestrator.getProcess('process-1')

        then:
            actual == new Process(id: new ObjectId('111111111111111111111111'), name: 'process-1')
    }

    def 'Process getProcess(ObjectId id)'() {
        setup:
            orchestrator.processRepository = processRepository

        when:
            processRepository.findByIdAndActiveIsTrue(new ObjectId('111111111111111111111111')) >> [ new Process(id: new ObjectId('111111111111111111111111'), name: 'process-1') ]
            Process actual = orchestrator.getProcess(new ObjectId('111111111111111111111111'))

        then:
            actual == new Process(id: new ObjectId('111111111111111111111111'), name: 'process-1')
    }

    def 'Collection<Process> getProcessesWaitingFor(String eventName)'() {
        setup:
            Process process1 = new Process(id: new ObjectId('111111111111111111111111'), name: 'process-1', definition: 'waiting for: \'event-1\'\nwaiting for: \'event-2\'')
            Process process2 = new Process(id: new ObjectId('222222222222222222222222'), name: 'process-2', definition: 'waiting for: \'event-2\'\nwaiting for: \'event-3\'')
            orchestrator.processRepository = processRepository

        when:
            processRepository.findByActiveIsTrue() >> [ process1, process2 ]
            Collection<Process> actual = orchestrator.getProcessesWaitingFor('event-1')

        then:
            actual.size() == 1
            actual[0] == process1
    }

    def 'Collection<Process> getProcessesListeningTo(Process outputProcess)'() {
        setup:
            Process process1 = new Process(id: new ObjectId('111111111111111111111111'), name: 'process-1', definition: 'listen to: \'process-1\'\nlisten to: \'process-2\'')
            Process process2 = new Process(id: new ObjectId('222222222222222222222222'), name: 'process-2', definition: 'listen to: \'process-2\'\nlisten to: \'process-3\'')
            orchestrator.processRepository = processRepository

        when:
            processRepository.findByActiveIsTrue() >> [ process1, process2 ]
            Collection<Process> actual = orchestrator.getProcessesListeningTo(new Process(name: 'process-1'))

        then:
            actual.size() == 1
            actual[0] == process1
    }

    def 'Collection<String> findEventsOneWaitsFor(Process process)'() {
        setup:
            Process process0 = new Process(id: new ObjectId('111111111111111111111111'), name: 'process-0')
            process0.definition = "command\nwaiting for: 'process-1'\ncommand\nlisten to: 'process-2'\ncommand"

        when:
            Collection<Process> actual = orchestrator.findEventsOneWaitsFor(process0)

        then:
            actual.size() == 1
            actual[0] == 'process-1'
    }

    def 'Collection<String> findProcessesOneListensTo(Process process)'() {
        setup:
            Process process0 = new Process(id: new ObjectId('111111111111111111111111'), name: 'process-0')
            process0.definition = "command\nwaiting for: 'process-1'\ncommand\nlisten to: 'process-2'\ncommand"

        when:
            Collection<Process> actual = orchestrator.findProcessesOneListensTo(process0)

        then:
            actual.size() == 1
            actual[0] == 'process-2'
    }
}
