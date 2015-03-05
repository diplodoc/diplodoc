package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRunParameter
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

            String script = processRun.process.definition
            Map<String, Object> parameters = processRun.parameters.collectEntries { ProcessRunParameter parameter ->
                [ parameter.key,  Class.forName(parameter.type).newInstance(jsonSlurper.parseText(parameter.value)) ]
            }
            println "process definition\n${script}"

            new GroovyShell(binding(parameters)).evaluate(script)

            println "process succeeded ${processRun}"
            diploexec.notify(ProcessCallEvent.succeed(processRun))
        } catch (e) {
            println "process failed ${processRun}"
            e.printStackTrace()
            diploexec.notify(ProcessCallEvent.failed(processRun))
        }
    }

    private Binding binding(Map<String, Object> parameters) {
        Binding binding = new Binding()

        bindInputParameters binding, parameters
        bindInput binding

        bindGet binding
        bindPost binding

        bindSend binding
        bindOutput binding
        bindNotify binding
        bindListen binding
        bindWaiting binding

        return binding
    }

    private void bindInputParameters(Binding binding, Map<String, Object> parameters) {
        parameters.each {
            binding."${it.key}" = it.value
        }
    }

    private void bindInput(Binding binding) {
        binding.input = { String[] args ->
            args.each { arg ->
                if (binding."${arg}" == null) {
                    throw new RuntimeException("Input parameter ${arg} is missing")
                }
            }
        }
    }

    private void bindGet(Binding binding) {
        binding.get = { Map params ->
            println "get with ${params}"
            String url = params.from
            Class responseType = params.expect ?: String

            restTemplate.getForObject(url, responseType)
        }
    }

    private void bindPost(Binding binding) {
        binding.post = { Map params ->
            println "post with ${params}"
            String url = params.to
            Object request = params.request
            Class responseType = params.expect ?: String

            restTemplate.postForObject(url, request, responseType)
        }
    }

    private void bindSend(Binding binding) {
        binding.send = { Map<String, Object> parameters ->
            String destination = parameters.to
            parameters.remove 'to'

            diploexec.notify(new SendEvent(destination, parameters))
        }
    }

    private void bindOutput(Binding binding) {
        binding.output = {Map<String, Object> parameters ->
            diploexec.notify(new OutputEvent(processRun, parameters))
        }
    }

    private void bindNotify(Binding binding) {
        binding.notify = { Map<String, Object> parameters ->
            String eventName = parameters.that
            parameters.remove 'that'

            diploexec.notify(new NotifyEvent(eventName, parameters))
        }
    }

    private void bindListen(Binding binding) {
        binding.listen = { /* do nothing */ }
    }

    private void bindWaiting(Binding binding) {
        binding.waiting = { /* do nothing */ }
    }
}
