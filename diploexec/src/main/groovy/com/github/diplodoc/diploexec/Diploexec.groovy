package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRepository
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRunRepository
import com.github.diplodoc.diplocore.modules.Bindable
import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class Diploexec {

    ThreadPoolTaskExecutor threadPool
    ApplicationContext modulesContext
    ProcessRepository processRepository
    ProcessRunRepository processRunRepository

    Collection<Process> processes
    Map<Process, Collection<String>> waitingMap
    Map<Process, Collection<String>> outputMap

    @PostConstruct
    void init() {
        log.info('initializing diploexec runtime...')

        log.debug('loading processes...')
        processes = processRepository.findByActiveIsTrue()
        waitingMap = new HashMap<>()
        outputMap = new HashMap<>()

        log.debug('creating process interaction map...')
        processes.each { Process process ->
            waitingMap[process] = waitsFor(process)
            outputMap[process] = inputFor(process)
        }
    }

    void run(ProcessRun processRun) {
        log.info('starting process {}...', processRun)
        threadPool.execute(new ProcessCall(this, processRun))
    }

    void notify(DiploexecEvent event) {
        log.info('event fired {}...', event)
        event.notifiedRuns(this).each { ProcessRun processRun -> run(processRun) }
    }

    void notify(ProcessCallEvent event) {
        log.info('event fired {}...', event)

        switch (event.type) {
            case ProcessCallEvent.Type.PROCESS_RUN_STARTED:
                event.processRun.exitStatus = 'NOT FINISHED'
                event.processRun.startTime = event.time.toString()
                processRunRepository.save event.processRun
            break;

            case ProcessCallEvent.Type.PROCESS_RUN_SUCCEED:
                event.processRun.exitStatus = 'SUCCEED'
                event.processRun.endTime = event.time.toString()
                processRunRepository.save event.processRun
            break;

            case ProcessCallEvent.Type.PROCESS_RUN_FAILED:
                event.processRun.exitStatus = 'FAILED'
                event.processRun.endTime = event.time.toString()
                processRunRepository.save event.processRun
                break;

            default:
                assert false : "unknown ProcessCallEvent: ${event.type}"
        }
    }

    Bindable getModule(String name) {
        modulesContext.getBean(name)
    }

    Process getProcess(String name) {
        processes.find { Process process -> process.name == name }
    }

    Collection<Process> getWaitProcesses(String eventName) {
        waitingMap.findAll { Process process, Collection<String> waitsFor -> waitsFor.contains(eventName) }.keySet()
    }

    Collection<Process> getInputProcesses(Process outputProcess) {
        outputMap.findAll { Process process, Collection<String> inputFor -> inputFor.contains(outputProcess.name) }.keySet()
    }

    Collection<String> waitsFor(Process process) {
        Collection<String> waitsFor = []

        String processWaitingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('waiting') }).join('\n')
        Binding binding = new Binding()
        binding.waiting = { Map parameters -> waitsFor << parameters.for }
        new GroovyShell(binding).evaluate(processWaitingDefinition)

        return waitsFor
    }

    Collection<String> inputFor(Process process) {
        Collection<String> inputFor = []

        String processListenDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('listen') }).join('\n')
        Binding binding = new Binding()
        binding.listen = { Map parameters -> inputFor << parameters.to }
        new GroovyShell(binding).evaluate(processListenDefinition)

        return inputFor
    }
}
