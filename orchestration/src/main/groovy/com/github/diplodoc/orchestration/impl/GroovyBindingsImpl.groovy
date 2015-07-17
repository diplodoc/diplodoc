package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import org.springframework.web.client.RestTemplate

import java.util.concurrent.TimeUnit

/**
 * @author yaroslav.yermilov
 */
class GroovyBindingsImpl implements GroovyBindings {

    RestTemplate restTemplate

    ProcessInteractor processInteractor

    @Override
    Binding executionBinding(ProcessRun processRun, Process process, Map input) {
        Binding binding = new Binding()

        bindInputParameters(binding, input)
        bindInput(binding)

        bindGet(binding)
        bindPost(binding)

        bindSend(binding)
        bindOutput(binding, process)
        bindEmit(binding)

        bindListen(binding)
        bindWaiting(binding)
        bindStart(binding)

        return binding
    }

    private void bindInputParameters(Binding binding, Map parameters) {
        parameters.each { key, value ->
            binding."${key}" = value
        }
    }

    private void bindInput(Binding binding) {
        binding.input = { String[] args ->
            args.each { arg ->
                if (!binding.hasVariable(arg)) {
                    throw new RuntimeException("Input parameter ${arg} is missing")
                }
            }
        }
    }

    private void bindGet(Binding binding) {
        binding.get = this.&get
    }

    private void bindPost(Binding binding) {
        binding.post = this.&post
    }

    private void bindSend(Binding binding) {
        binding.send = this.&send
    }

    private void bindOutput(Binding binding, Process process) {
        binding.output = { Map params ->
            processInteractor.output(process, params)
        }
    }

    private void bindEmit(Binding binding) {
        binding.emit = this.&emit
    }

    private void bindListen(Binding binding) {
        binding.listen = { /* do nothing */ }
    }

    private void bindWaiting(Binding binding) {
        binding.waiting = { /* do nothing */ }
    }

    private void bindStart(Binding binding) {
        binding.start = { /* do nothing */ }

        Integer.metaClass.propertyMissing = {String name ->
            TimeUnit timeUnit = TimeUnit.valueOf(name.toUpperCase())
            if (timeUnit != null) {
                return timeUnit.toMillis(delegate)
            }
        }
    }

    private def get(Map params) {
        String root = params.root
        String path = params.from
        Class responseType = params.expect ?: String

        String url = "${System.getProperty 'modules_host'}/$root/$path"

        restTemplate.getForObject(url, responseType)
    }

    private def post(Map params) {
        String root = params.root
        String path = params.to
        Object request = params.request
        Class responseType = params.expect ?: String

        String url = "${System.getProperty 'modules_host'}/$root/$path"

        restTemplate.postForObject(url, request, responseType)
    }

    private void send(Map params) {
        String destination = params.remove 'to'
        processInteractor.send(destination, params)
    }

    private void emit(Map params) {
        String eventName = params.remove 'that'
        processInteractor.emit(eventName, params)
    }
}
