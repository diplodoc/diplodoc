package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRunParameter
import com.github.diplodoc.orchestration.old.NotifyEvent
import com.github.diplodoc.orchestration.old.OldOrchestratorImpl
import com.github.diplodoc.orchestration.old.OutputEvent
import com.github.diplodoc.orchestration.old.ProcessCall
import com.github.diplodoc.orchestration.old.ProcessCallEvent
import com.github.diplodoc.orchestration.old.SendEvent
import groovy.json.JsonOutput
import org.bson.types.ObjectId
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCallSpec extends Specification {

    def 'void run() - successful run'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)

            Process process = new Process(id: new ObjectId('111111111111111111111111'), definition: 'definition')
            ProcessRun processRun = new ProcessRun(processId: new ObjectId('111111111111111111111111'), parameters: [])
            processRun.parameters << new ProcessRunParameter(key: 'key-1', type: String.name, value: JsonOutput.toJson('someValue'))
            processRun.parameters << new ProcessRunParameter(key: 'key-2', type: Integer.name, value: JsonOutput.toJson(28))

            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])
            1 * orchestrator.getProcess(new ObjectId('111111111111111111111111')) >> process
            1 * processCall.evaluate([ 'key-1': 'someValue', 'key-2': 28 ], 'definition') >> {}
            1 * orchestrator.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED })
            1 * orchestrator.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_SUCCEED })

        expect:
            processCall.run()
    }

    def 'void run() - failed run'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)

            Process process = new Process(id: new ObjectId('111111111111111111111111'), definition: 'definition')
            ProcessRun processRun = new ProcessRun(processId: new ObjectId('111111111111111111111111'), parameters: [])
            processRun.parameters << new ProcessRunParameter(key: 'key-1', type: String.name, value: JsonOutput.toJson('someValue'))
            processRun.parameters << new ProcessRunParameter(key: 'key-2', type: Integer.name, value: JsonOutput.toJson(28))

            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])
            1 * orchestrator.getProcess(new ObjectId('111111111111111111111111')) >> process
            1 * processCall.evaluate([ 'key-1': 'someValue', 'key-2': 28 ], 'definition') >> { throw new RuntimeException() }
            1 * orchestrator.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_STARTED })
            1 * orchestrator.notify({ ProcessCallEvent event -> event.processRun == processRun && event.type == ProcessCallEvent.Type.PROCESS_RUN_FAILED })

        expect:
            processCall.run()
    }

    def 'Binding binding(Map<String, Object> parameters)'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

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
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

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
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

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
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

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
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.get(from: 'url', root: 'root', expect: Integer)

        then:
            1 * restTemplate.getForObject('null/root/url', Integer)
    }

    def 'void get(Map params) - default response type'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.get(from: 'url', root: 'root')

        then:
            1 * restTemplate.getForObject('null/root/url', String)
    }

    def 'void post(Map params)'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.post(to: 'url', root: 'root', request: 'request', expect: Integer)

        then:
            1 * restTemplate.postForObject('null/root/url', 'request', Integer)
    }

    def 'void post(Map params) - default response type'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

            RestTemplate restTemplate = Mock(RestTemplate)
            processCall.restTemplate = restTemplate

        when:
            processCall.post(to: 'url', root: 'root', request: 'request')

        then:
            1 * restTemplate.postForObject('null/root/url', 'request', String)
    }

    def 'void send(Map params)'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

        when:
            processCall.send('to': 'destination', 'key': 'value')

        then:
            1 * orchestrator.notify(new SendEvent('destination', [ 'key': 'value' ]))
    }

    def 'void output(Map params)'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun(id: new ObjectId('111111111111111111111111'))
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

        when:
            processCall.output('key': 'value')

        then:
            1 * orchestrator.notify(new OutputEvent(processRun, [ 'key': 'value' ]))
    }

    def 'void notify(Map params)'() {
        setup:
            OldOrchestratorImpl orchestrator = Mock(OldOrchestratorImpl)
            ProcessRun processRun = new ProcessRun()
            ProcessCall processCall = Spy(ProcessCall, constructorArgs: [ orchestrator, processRun ])

        when:
            processCall.notify('that': 'event', 'key': 'value')

        then:
            1 * orchestrator.notify(new NotifyEvent('event', [ 'key': 'value' ]))
    }
}
