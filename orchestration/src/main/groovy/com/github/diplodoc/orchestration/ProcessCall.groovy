package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRunParameter
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class ProcessCall implements Runnable {

    JsonSlurper jsonSlurper = new JsonSlurper()
    RestTemplate restTemplate = new RestTemplate()

    Orchestrator orchestrator
    ProcessRun processRun

    ProcessCall(Orchestrator orchestrator, ProcessRun processRun) {
        this.orchestrator = orchestrator
        this.processRun = processRun
    }

    @Override
    void run() {
        try {
            log.info "process started ${processRun}"
            orchestrator.notify(ProcessCallEvent.started(processRun))

            String script = orchestrator.getProcess(processRun.processId).definition
            Map<String, Object> parameters = processRun.parameters.collectEntries { ProcessRunParameter parameter ->
                [ parameter.key,  Class.forName(parameter.type).newInstance(jsonSlurper.parseText(parameter.value)) ]
            }
            log.info "process definition\n${script}"

            evaluate(parameters, script)

            log.info "process succeeded ${processRun}"
            orchestrator.notify(ProcessCallEvent.succeed(processRun))
        } catch (e) {
            log.error "process failed ${processRun}", e
            orchestrator.notify(ProcessCallEvent.failed(processRun))
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

    def get(Map params) {
        String root = params.root
        String path = params.from
        Class responseType = params.expect ?: String

        String url = "${System.getProperty 'modules_host'}/$root/$path"

        restTemplate.getForObject(url, responseType)
    }

    def post(Map params) {
        String root = params.root
        String path = params.to
        Object request = params.request
        Class responseType = params.expect ?: String

        String url = "${System.getProperty 'modules_host'}/$root/$path"

        restTemplate.postForObject(url, request, responseType)
    }

    void send(Map params) {
        String destination = params.remove 'to'
        orchestrator.notify(new SendEvent(destination, params))
    }

    void output(Map params) {
        orchestrator.notify(new OutputEvent(processRun, params))
    }

    void notify(Map params) {
        String eventName = params.remove 'that'
        orchestrator.notify(new NotifyEvent(eventName, params))
    }
}
