package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class Orchestrator {

    ThreadPoolTaskExecutor threadPool
    ProcessRepository processRepository
    ProcessRunRepository processRunRepository

    ObjectId run(ObjectId processId, List parameters) {
        ProcessRun processRun = processRunRepository.save new ProcessRun(processId: processId, parameters: parameters)

        log.info "starting process ${processRun}..."
        threadPool.execute(new ProcessCall(this, processRun))

        return processRun.id
    }

    void notify(OrchestrationEvent event) {
        log.info "event fired ${event}..."
        event.shouldNotifyRuns(this).each { ProcessRun processRun -> run(processRun.processId, processRun.parameters) }
    }

    void notify(ProcessCallEvent event) {
        log.info "event fired ${event}..."

        switch (event.type) {
            case ProcessCallEvent.Type.PROCESS_RUN_STARTED:
                event.processRun.exitStatus = 'NOT FINISHED'
                event.processRun.startTime = event.time
                processRunRepository.save event.processRun
            break

            case ProcessCallEvent.Type.PROCESS_RUN_SUCCEED:
                event.processRun.exitStatus = 'SUCCEED'
                event.processRun.endTime = event.time
                processRunRepository.save event.processRun
            break

            case ProcessCallEvent.Type.PROCESS_RUN_FAILED:
                event.processRun.exitStatus = 'FAILED'
                event.processRun.endTime = event.time
                processRunRepository.save event.processRun
            break

            default:
                assert false : "unknown ProcessCallEvent: ${event.type}"
        }
    }

    Process getProcess(String name) {
        processRepository.findByNameAndActiveIsTrue(name)?.first()
    }

    Process getProcess(ObjectId id) {
        processRepository.findByIdAndActiveIsTrue(id)?.first()
    }

    Collection<Process> getProcessesWaitingFor(String eventName) {
        processRepository.findByActiveIsTrue().findAll { Process process ->
            findEventsOneWaitsFor(process).contains(eventName)
        }
    }

    Collection<Process> getProcessesListeningTo(Process outputProcess) {
        processRepository.findByActiveIsTrue().findAll { Process process ->
            findProcessesOneListensTo(process).contains(outputProcess.name)
        }
    }

    Collection<String> findEventsOneWaitsFor(Process process) {
        Collection<String> waitsFor = []

        String processWaitingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('waiting') }).join('\n')
        Binding binding = new Binding()
        binding.waiting = { Map parameters -> waitsFor << parameters.for }
        new GroovyShell(binding).evaluate(processWaitingDefinition)

        return waitsFor
    }

    Collection<String> findProcessesOneListensTo(Process process) {
        Collection<String> inputFor = []

        String processListenDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('listen') }).join('\n')
        Binding binding = new Binding()
        binding.listen = { Map parameters -> inputFor << parameters.to }
        new GroovyShell(binding).evaluate(processListenDefinition)

        return inputFor
    }
}
