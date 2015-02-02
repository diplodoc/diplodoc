package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRunParameter
import com.github.diplodoc.diplocore.modules.Bindable
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class ProcessCall implements Runnable {

    JsonSlurper jsonSlurper = new JsonSlurper()

    Diploexec diploexec
    ProcessRun processRun

    ProcessCall(Diploexec diploexec, ProcessRun processRun) {
        this.diploexec = diploexec
        this.processRun = processRun
    }

    @Override
    void run() {
        try {
            log.info('process started {}', processRun)
            diploexec.notify(ProcessCallEvent.started(processRun))

            String script = processRun.process.definition
            Map<String, Object> parameters = processRun.parameters.collectEntries { ProcessRunParameter parameter ->
                [ parameter.key,  Class.forName(parameter.type).newInstance(jsonSlurper.parseText(parameter.value)) ]
            }
            log.debug('process definition {}', script)

            new GroovyShell(binding(parameters)).evaluate(script)

            log.info('process succeeded {}', processRun)
            diploexec.notify(ProcessCallEvent.succeed(processRun))
        } catch (e) {
            log.warn('process failed {}', processRun)
            diploexec.notify(ProcessCallEvent.failed(processRun))
        }
    }

    private Binding binding(Map<String, Object> parameters) {
        Binding binding = new Binding()

        bindInputParameters binding, parameters
        bindInput binding
        bindDescription binding
        bindRequire binding
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
        binding.input = { String[] args -> /* do nothing */ }
    }

    private void bindDescription(Binding binding) {
        binding.description = { String description -> /* do nothing */ }
    }

    private void bindRequire(Binding binding) {
        binding.require = { String[] modulesNames ->
            modulesNames.each { String moduleName ->
                Bindable module = diploexec.getModule(moduleName)
                module.bindSelf binding
            }
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
