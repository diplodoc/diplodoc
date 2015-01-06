package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Module
import com.github.diplodoc.diplobase.repository.diploexec.ModuleRepository
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.annotation.PostConstruct

/**
 * @author yaroslav.yermilov
 */
class DiploexecRuntimeEnvironment {

    ModuleRepository moduleRepository
    ApplicationContext modulesContext
    ThreadPoolTaskExecutor threadPool

    List<Module> modules
    List<ModuleRun> runs

    @PostConstruct
    def run() {
        println "Starting Diploexec Runtime Environment..."

        modules = retrieveModules()
        runs = []
    }

    def startModule(Long id, Map input) {
        println "Starting module [${id}] with parameters [${input}]..."

        def module = modules.find { it.id == id }

        def run = new ModuleRun(runtime: this, module: module, params: input)
        runs << run

        threadPool.execute({
            run.run()
            println "Module [${module.name}] finished"
        })
    }

    def status() {
        runs.collect {
            def status = [:]
            status.id = it.id
            status.name = it.module.name
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

    def outputed(Module module, Map params) {
        modules.findAll { it.listensTo.contains(module.name) } .each {
            startModule(it.name, params)
        }
    }

    def notified(String eventName, Map params) {
        modules.findAll { it.waitingFor.contains(eventName) }.each {
            startModule(it.name, params)
        }
    }

    private def retrieveModules() {
        println "Retrieving modules..."

        def modules = moduleRepository.findAll()
        modules.each {
            it.listensTo = getListenTo(it)
            it.waitingFor = getWaitingFor(it)
        }

        println "${modules.size()} modules retrieved:"
        println "id".padRight(5).plus("name".padRight(40)).plus("Listens to".padRight(60)).plus("Waiting for".padRight(60))
        modules.each {
            println "${it.id}".padRight(5).plus("${it.name}".padRight(40)).plus("${it.listensTo}".padRight(60)).plus("${it.waitingFor}".padRight(60))
        }

        return modules
    }

    private def getListenTo(Module module) {
        def moduleListenDefinition = module.definition.readLines().collect {
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

        new GroovyShell(binding).evaluate(moduleListenDefinition)
        result
    }

    private def getWaitingFor(Module module) {
        def moduleListenDefinition = module.definition.readLines().collect {
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

        new GroovyShell(binding).evaluate(moduleListenDefinition)
        result
    }
}
