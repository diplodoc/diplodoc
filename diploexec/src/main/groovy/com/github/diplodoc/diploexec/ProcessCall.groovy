package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Process
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRunParameter
import groovy.json.JsonSlurper
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class ProcessCall implements Runnable {

    JsonSlurper jsonSlurper = new JsonSlurper()
    RestTemplate restTemplate = new RestTemplate()

    Diploexec diploexec
    ProcessRun processRun

    ProcessCall(Diploexec diploexec, ProcessRun processRun) {
        this.diploexec = diploexec
        this.processRun = processRun
    }

    @Override
    void run() {
        try {
            println "process started ${processRun}"
            diploexec.notify(ProcessCallEvent.started(processRun))

            String script = diploexec.getProcess(processRun.processId).definition
            Map<String, Object> parameters = processRun.parameters.collectEntries { ProcessRunParameter parameter ->
                [ parameter.key,  Class.forName(parameter.type).newInstance(jsonSlurper.parseText(parameter.value)) ]
            }
            println "process definition\n${script}"

            evaluate(parameters, script)

            println "process succeeded ${processRun}"
            diploexec.notify(ProcessCallEvent.succeed(processRun))
        } catch (e) {
            println "process failed ${processRun}"
            e.printStackTrace()
            diploexec.notify(ProcessCallEvent.failed(processRun))
        }
    }

    void evaluate(Map parameters, String script) {
        new GroovyShell(binding(parameters)).evaluate(script)
    }

    Binding binding(Map<String, Object> parameters) {
        Binding binding = new Binding()

        bindInputParameters binding, parameters
        bindInput binding

        binding.get = this.&get
        binding.post = this.&post

        binding.send = this.&send
        binding.output = this.&output
        binding.notify = this.&notify
        binding.listen = { /* do nothing */ }
        binding.waiting = { /* do nothing */ }

        return binding
    }

    void bindInputParameters(Binding binding, Map parameters) {
        parameters.each {
            binding."${it.key}" = it.value
        }
    }

    void bindInput(Binding binding) {
        binding.input = { String[] args ->
            args.each { arg ->
                if (!binding.hasVariable(arg)) {
                    throw new RuntimeException("Input parameter ${arg} is missing")
                }
            }
        }
    }

    void get(Map params) {
        String url = params.from
        Class responseType = params.expect ?: String

        restTemplate.getForObject(url, responseType)
    }

    void post(Map params) {
        String url = params.to
        Object request = params.request
        Class responseType = params.expect ?: String

        restTemplate.postForObject(url, request, responseType)
    }

    void send(Map params) {
        String destination = params.remove 'to'
        diploexec.notify(new SendEvent(destination, params))
    }

    void output(Map params) {
        diploexec.notify(new OutputEvent(processRun, params))
    }

    void notify(Map params) {
        String eventName = params.remove 'that'
        diploexec.notify(new NotifyEvent(eventName, params))
    }
}
