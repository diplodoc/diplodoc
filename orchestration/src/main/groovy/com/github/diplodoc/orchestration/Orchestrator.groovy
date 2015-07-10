package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

import javax.annotation.PostConstruct
import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class Orchestrator {

    ThreadPoolTaskScheduler scheduler
    ProcessRepository processRepository
    ProcessRunRepository processRunRepository

    @PostConstruct
    void init() {
        log.info "initializing orchestrator..."

        processRepository.findByActiveIsTrue()
                .findAll({ Process process -> findSchedulingPeriod(process) != null })
                .each({ Process process -> run(process.id, []) })
    }

    ObjectId run(ObjectId processId, List parameters) {
        ProcessRun processRun = processRunRepository.save new ProcessRun(processId: processId, parameters: parameters)

        log.info "starting process ${processRun}..."
        scheduler.execute(new ProcessCall(this, processRun))

        return processRun.id
    }

    ObjectId run(ObjectId processId, long delay) {
        ProcessRun processRun = processRunRepository.save new ProcessRun(processId: processId, parameters: [])
        Date startTime = new Date(System.currentTimeMillis() + delay)

        log.info "scheduling process ${processRun} to start at ${startTime}..."
        scheduler.schedule(new ProcessCall(this, processRun), startTime)

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

                def scheduling = findSchedulingPeriod(getProcess(event.processRun.processId))
                if (scheduling != null) {
                    run(event.processRun.processId, scheduling.period)
                }
            break

            case ProcessCallEvent.Type.PROCESS_RUN_FAILED:
                event.processRun.exitStatus = 'FAILED'
                event.processRun.endTime = event.time
                processRunRepository.save event.processRun

                def scheduling = findSchedulingPeriod(getProcess(event.processRun.processId))
                if (scheduling != null) {
                    run(event.processRun.processId, scheduling.period)
                }
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

    Map findSchedulingPeriod(Process process) {
        Map scheduling

        String processSchedulingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('start') }).join('\n')
        Binding binding = new Binding()
        binding.start = { Map parameters ->
            scheduling = [:]
            scheduling.period = parameters.remove 'every'
            scheduling.parameters = parameters
        }
        Integer.metaClass.propertyMissing = {String name ->
            TimeUnit timeUnit = TimeUnit.valueOf(name.toUpperCase())
            if (timeUnit != null) {
                return timeUnit.toMillis(delegate)
            }
        }
        new GroovyShell(binding).evaluate(processSchedulingDefinition)

        return scheduling
    }
}
