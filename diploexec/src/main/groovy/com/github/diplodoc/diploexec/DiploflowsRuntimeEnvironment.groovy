package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Flow
import com.github.diplodoc.diplobase.repository.diploexec.FlowRepository
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
class DiploflowsRuntimeEnvironment {

    FlowRepository flowRepository
    ApplicationContext modulesContext
    ThreadPoolTaskExecutor threadPool

    List<Flow> flows
    List<FlowRun> runs

    @PostConstruct
    def run() {
        println "Starting Diploflows Runtime Environment..."

        flows = retrieveFlows()
        runs = []
    }

    def startFlow(String name, Map input) {
        println "Starting flow [${name}] with parameters [${input}]..."

        def flow = flows.find { it.name == name }

        def run = new FlowRun(runtime: this, flow: flow, params: input)
        runs << run

        threadPool.execute({
            run.run()
            println "Flow [${flow.name}] finished"
        })
    }

    def status() {
        runs.collect {
            def status = [:]
            status.id = it.id
            status.name = it.flow.name
            status.description = it.description
            status.status = it.status
            status.startTime = it.startTime.toString()
            status.endTime = it.endTime.toString()

            status
        }
    }

    def getModule(String name) {
        modulesContext.getBean(name)
    }

    def outputed(Flow flow, Map params) {
        flows.findAll { it.listensTo.contains(flow.name) } .each {
            startFlow(it.name, params)
        }
    }

    def notified(String eventName, Map params) {
        flows.findAll { it.waitingFor.contains(eventName) }.each {
            startFlow(it.name, params)
        }
    }

    private def retrieveFlows() {
        println "Retrieving flows..."

        def flows = flowRepository.findAll()
        flows.each {
            it.listensTo = getListenTo(it)
            it.waitingFor = getWaitingFor(it)
        }

        println "${flows.size()} flows retrieved:"
        println "id".padRight(5).plus("name".padRight(40)).plus("Listens to".padRight(60)).plus("Waiting for".padRight(60))
        flows.each {
            println "${it.id}".padRight(5).plus("${it.name}".padRight(40)).plus("${it.listensTo}".padRight(60)).plus("${it.waitingFor}".padRight(60))
        }

        return flows
    }

    private def getListenTo(Flow flow) {
        def flowListenDefinition = flow.definition.readLines().collect {
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

        new GroovyShell(binding).evaluate(flowListenDefinition)
        result
    }

    private def getWaitingFor(Flow flow) {
        def flowListenDefinition = flow.definition.readLines().collect {
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

        new GroovyShell(binding).evaluate(flowListenDefinition)
        result
    }
}
