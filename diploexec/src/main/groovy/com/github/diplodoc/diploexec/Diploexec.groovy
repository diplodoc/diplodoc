package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRepository
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRunRepository
import com.github.diplodoc.diplocore.modules.Bindable
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
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
        processes = processRepository.findAll()
        waitingMap = new HashMap<>()
        outputMap = new HashMap<>()

        processes.each { Process process ->
            waitingMap[process] = waitsFor(process)
            outputMap[process] = inputFor(process)
        }
    }

    void run(ProcessRun processRun) {
        threadPool.execute(new ProcessCall(this, processRun))
    }

    void notify(DiploexecEvent event) {
        event.notifiedRuns(this).each { ProcessRun processRun -> run(processRun) }
    }

    void notify(ProcessCallEvent event) {
        switch (event.type) {
            case ProcessCallEvent.Type.PROCESS_RUN_STARTED:
                event.processRun.startTime = event.time.toString()
                processRunRepository.save event.processRun
            break;

            case ProcessCallEvent.Type.PROCESS_RUN_ENDED:
                event.processRun.endTime = event.time.toString()
                processRunRepository.save event.processRun
            break;
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

    Collection<Process> waitsFor(Process process) {
        Collection<Process> waitsFor = []

        String processWaitingDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('waiting') }).join('\n')
        Binding binding = new Binding()
        binding.waiting = { Map parameters -> waitsFor << parameters.for }
        new GroovyShell(binding).evaluate(processWaitingDefinition)

        return waitsFor
    }

    Collection<Process> inputFor(Process process) {
        Collection<Process> inputFor = []

        String processListenDefinition = process.definition.readLines().findAll({ String line -> line.startsWith('listen') }).join('\n')
        Binding binding = new Binding()
        binding.listen = { Map parameters -> inputFor << parameters.to }
        new GroovyShell(binding).evaluate(processListenDefinition)

        return inputFor
    }
}
