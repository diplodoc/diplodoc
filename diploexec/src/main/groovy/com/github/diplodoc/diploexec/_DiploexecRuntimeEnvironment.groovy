package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRepository
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
class _DiploexecRuntimeEnvironment {

    ProcessRepository processRepository
    ApplicationContext modulesContext
    ThreadPoolTaskExecutor threadPool

    List<Process> processes
    List<_ProcessRun> runs

    @PostConstruct
    def run() {
        println "Starting Diploexec Runtime Environment..."

        processes = retrieveProcesses()
        runs = []
    }

    def startProcess(Long id, Map input) {
        println "Starting process [${id}] with parameters [${input}]..."

        def process = processes.find { it.id == id }

        def run = new _ProcessRun(runtime: this, process: process, params: input)
        runs << run

        threadPool.execute({
            run.run()
            println "Process [${process.name}] finished"
        })
    }

    def status() {
        runs.collect {
            def status = [:]
            status.id = it.id
            status.name = it.process.name
            status.description = it.description
            status.status = it.status
            status.startTime = it.startTime.toString()
            status.endTime = it.endTime.toString()

            status
        }
    }

    def getProcess(String name) {
        processesContext.getBean(name)
    }

    def outputed(Process process, Map params) {
        processes.findAll { it.listensTo.contains(process.name) } .each {
            startProcess(it.name, params)
        }
    }

    def notified(String eventName, Map params) {
        processes.findAll { it.waitingFor.contains(eventName) }.each {
            startProcess(it.name, params)
        }
    }

    private def retrieveProcesses() {
        println "Retrieving processes..."

        def processes = processRepository.findAll()
        processes.each {
            it.listensTo = getListenTo(it)
            it.waitingFor = getWaitingFor(it)
        }

        println "${processes.size()} processes retrieved:"
        println "id".padRight(5).plus("name".padRight(40)).plus("Listens to".padRight(60)).plus("Waiting for".padRight(60))
        processes.each {
            println "${it.id}".padRight(5).plus("${it.name}".padRight(40)).plus("${it.listensTo}".padRight(60)).plus("${it.waitingFor}".padRight(60))
        }

        return processes
    }

    private def getListenTo(Process process) {
        def processListenDefinition = process.definition.readLines().collect {
            if (it.startsWith('listen')) {
                it
            } else {
                ""
            }
        }.join("")

        def result = []

        def binding = new Binding()
        binding.listen = {
            Map params ->
                result <<  params.to
        }

        new GroovyShell(binding).evaluate(processListenDefinition)
        result
    }

    private def getWaitingFor(Process process) {
        def processListenDefinition = process.definition.readLines().collect {
            if (it.startsWith('waiting')) {
                it
            } else {
                ""
            }
        }.join("")

        def result = []

        def binding = new Binding()
        binding.waiting = {
            Map params ->
                result <<  params.for
        }

        new GroovyShell(binding).evaluate(processListenDefinition)
        result
    }
}
