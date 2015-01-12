package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.client.ProcessDataClient
import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplocore.modules.Module
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
class Diploexec {

    ThreadPoolTaskExecutor threadPool

    ApplicationContext modulesContext
    ProcessDataClient processDataClient

    Collection<Process> processes
    Map<Process, Collection<String>> waitingMap = new HashMap<>()
    Map<Process, Collection<String>> outputMap = new HashMap<>()

    @PostConstruct
    void init() {
        processes = processDataClient.processes()
        processes.each { Process process ->
            waitingMap << waitsFor(process)
            outputMap << inputFor(process)
        }
    }

    ProcessRun run(ProcessRun processRun) {
        threadPool.submitListenable(new ProcessCall(processRun)).addCallback(new ProcessCallback())
    }

    void notify(DiploexecEvent event) {
        event.notifiedRuns().each { ProcessRun processRun ->
            run(processRun)
        }
    }

    Module getModule(String name) {
        modulesContext.getBean(name)
    }

    Process getProcess(String name) {
        processes.find { Process process -> process.name == name }
    }

    Collection<Process> getWaitProcesses(String eventName) {
        waitingMap
                .findAll { Process process, Collection<String> waitsFor -> waitsFor.contains(eventName) }
                .keySet()
    }

    Collection<Process> getInputProcesses(Process outputProcess) {
        outputMap
                .findAll { Process process, Collection<String> inputFor -> inputFor.contains(outputProcess.name) }
                .keySet()
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
