package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Process
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ProcessRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ProcessRunRepository
import org.bson.types.ObjectId
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
class Diploexec {

    ThreadPoolTaskExecutor threadPool
    ProcessRepository processRepository
    ProcessRunRepository processRunRepository

    Collection<Process> processes
    Map<Process, Collection<String>> waitsForEventsMap
    Map<Process, Collection<String>> listenToProcessesMap

    @PostConstruct
    void init() {
        println 'initializing diploexec runtime...'

        println 'loading processes...'
        processes = processRepository.findByActiveIsTrue()
        waitsForEventsMap = new HashMap<>()
        listenToProcessesMap = new HashMap<>()

        println 'creating process interaction map...'
        processes.each { Process process ->
            waitsForEventsMap[process] = findEventsOneWaitsFor(process)
            listenToProcessesMap[process] = findProcessesOneListensTo(process)
        }
    }

    ObjectId run(ObjectId processId, List parameters) {
        ProcessRun processRun = processRunRepository.save new ProcessRun(processId: processId, parameters: parameters)

        println "starting process ${processRun}..."
        threadPool.execute(new ProcessCall(this, processRun))

        return processRun.id
    }

    void notify(DiploexecEvent event) {
        println "event fired ${event}..."
        event.shouldNotifyRuns(this).each { ProcessRun processRun -> run(processRun.processId, processRun.parameters) }
    }

    void notify(ProcessCallEvent event) {
        println "event fired ${event}..."

        switch (event.type) {
            case ProcessCallEvent.Type.PROCESS_RUN_STARTED:
                event.processRun.exitStatus = 'NOT FINISHED'
                event.processRun.startTime = event.time
                processRunRepository.save event.processRun
            break;

            case ProcessCallEvent.Type.PROCESS_RUN_SUCCEED:
                event.processRun.exitStatus = 'SUCCEED'
                event.processRun.endTime = event.time
                processRunRepository.save event.processRun
            break;

            case ProcessCallEvent.Type.PROCESS_RUN_FAILED:
                event.processRun.exitStatus = 'FAILED'
                event.processRun.endTime = event.time
                processRunRepository.save event.processRun
                break;

            default:
                assert false : "unknown ProcessCallEvent: ${event.type}"
        }
    }

    Process getProcess(String name) {
        processes.find { Process process -> process.name == name }
    }

    Process getProcess(ObjectId id) {
        processes.find { Process process -> process.id == id }
    }

    Collection<Process> getProcessesWaitingFor(String eventName) {
        waitsForEventsMap.findAll { Process process, Collection<String> waitsFor -> waitsFor.contains(eventName) }.keySet()
    }

    Collection<Process> getProcessesListeningTo(Process outputProcess) {
        listenToProcessesMap.findAll { Process process, Collection<String> inputFor -> inputFor.contains(outputProcess.name) }.keySet()
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
