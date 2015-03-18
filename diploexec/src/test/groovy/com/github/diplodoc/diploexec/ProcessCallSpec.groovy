package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRunParameter
import groovy.json.JsonOutput
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCallSpec extends Specification {

    def 'void run() - successful run'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)

            Process process = new Process(definition: 'definition')
            ProcessRun processRun = new ProcessRun(process: process, parameters: [])
            processRun.parameters << new ProcessRunParameter(key: 'key-1', type: String.name, value: JsonOutput.toJson('someValue'))
            processRun.parameters << new ProcessRunParameter(key: 'key-2', type: Integer.name, value: JsonOutput.toJson(28))

            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])
            1 * processCall.evaluate([ 'key-1': 'someValue', 'key-2': 28 ], 'definition') >> {}
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED })
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_SUCCEED })

        expect:
            processCall.run()
    }

    def 'void run() - failed run'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)

            Process process = new Process(definition: 'definition')
            ProcessRun processRun = new ProcessRun(process: process, parameters: [])
            processRun.parameters << new ProcessRunParameter(key: 'key-1', type: String.name, value: JsonOutput.toJson('someValue'))
            processRun.parameters << new ProcessRunParameter(key: 'key-2', type: Integer.name, value: JsonOutput.toJson(28))

            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])
            1 * processCall.evaluate([ 'key-1': 'someValue', 'key-2': 28 ], 'definition') >> { throw new RuntimeException() }
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED })
            1 * diploexec.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_FAILED })

        expect:
            processCall.run()
    }

    def 'Binding binding(Map<String, Object> parameters)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            Map parameters = [ 'key1': 'value1', 'key2': 28 ]

        when:
            Binding binding = processCall.binding(parameters)

        then:
            binding.hasVariable('key1')
            binding.hasVariable('key2')
            binding.hasVariable('input')
            binding.hasVariable('get')
            binding.hasVariable('post')
            binding.hasVariable('send')
            binding.hasVariable('output')
            binding.hasVariable('notify')
            binding.hasVariable('listen')
            binding.hasVariable('waiting')
    }

    def 'void bindInputParameters(Binding binding, Map parameters)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            Binding binding = new Binding()
            Map parameters = [ 'key1': 'value1', 'key2': 28 ]

        when:
            processCall.bindInputParameters(binding, parameters)

        then:
            binding.key1 == 'value1'
            binding.key2 == 28
    }

    def 'void input(String[] args) - all parameters exists'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            Binding binding = new Binding()
            Map parameters = [ 'key1': 'value1', 'key2': 28 ]

            processCall.bindInputParameters(binding, parameters)
            processCall.bindInput(binding)

        when:
           binding.input.call([ 'key1', 'key2' ] as String[])

        then:
            notThrown(Exception)
    }

    def 'void input(String[] args) - missing parameter'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            Binding binding = new Binding()
            Map parameters = [ 'key1': 'value1', 'key2': 28 ]

            processCall.bindInputParameters(binding, parameters)
            processCall.bindInput(binding)

        when:
            binding.input.call([ 'key1', 'key2', 'key3' ] as String[])

        then:
            def e = thrown(RuntimeException)
            e.message == 'Input parameter key3 is missing'
    }

    def 'void get(Map params)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.get(from: 'url', expect: Integer)

        then:
            1 * restTemplate.getForObject('url', Integer)
    }

    def 'void get(Map params) - default response type'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.get(from: 'url')

        then:
            1 * restTemplate.getForObject('url', String)
    }

    def 'void post(Map params)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.post(to: 'url', request: 'request', expect: Integer)

        then:
            1 * restTemplate.postForObject('url', 'request', Integer)
    }

    def 'void post(Map params) - default response type'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.post(to: 'url', request: 'request')

        then:
            1 * restTemplate.postForObject('url', 'request', String)
    }

    def 'void send(Map params)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

        when:
            processCall.send('to': 'destination', 'key': 'value')

        then:
            1 * diploexec.notify(new SendEvent('destination', [ 'key': 'value' ]))
    }

    def 'void output(Map params)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun(id: 28)
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

        when:
            processCall.output('key': 'value')

        then:
            1 * diploexec.notify(new OutputEvent(processRun, [ 'key': 'value' ]))
    }

    def 'void notify(Map params)'() {
        setup:
            Diploexec diploexec = Mock(Diploexec)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ diploexec, processRun ])

        when:
            processCall.notify('that': 'event', 'key': 'value')

        then:
            1 * diploexec.notify(new NotifyEvent('event', [ 'key': 'value' ]))
    }
}
