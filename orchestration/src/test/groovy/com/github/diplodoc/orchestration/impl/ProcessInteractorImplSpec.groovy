package com.github.diplodoc.orchestration.impl

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessRunner
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessInteractorImplSpec extends Specification {

    private final static ObjectId ID_1 = new ObjectId('111111111111111111111111')
    private final static ObjectId ID_2 = new ObjectId('222222222222222222222222')
    private final static ObjectId ID_3 = new ObjectId('333333333333333333333333')
    private final static ObjectId ID_4 = new ObjectId('444444444444444444444444')
    private final static ObjectId ID_5 = new ObjectId('555555555555555555555555')

    ProcessRunner processRunner = Mock(ProcessRunner)
    ProcessRepository processRepository = Mock(ProcessRepository)
    GroovyBindings groovyBindings = Mock(GroovyBindings)

    ProcessInteractorImpl processInteractor = Spy(ProcessInteractorImpl)

    def 'Collection<ProcessRun> processSelfStart()'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            Process process1 = new Process(id: ID_1)
            Process process2 = new Process(id: ID_2)

            ProcessRun processRun1 = new ProcessRun(id: ID_3)

        when:
            1 * processRepository.findByActiveIsTrue() >> [ process1, process2 ]
            1 * processInteractor.isSelfStarting(process1) >> true
            1 * processInteractor.isSelfStarting(process2) >> false

            1 * processRunner.start(process1) >> processRun1
            0 * processRunner.start(process2)

            def actual = processInteractor.processSelfStart()

        then:
            actual == [ processRun1 ]
    }

    def 'ProcessRun send(String destination, Map params)'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            String destination = 'destination'
            Map params = [ key: 'value' ]

            Process process = new Process(id: ID_1)

            ProcessRun processRun = new ProcessRun(id: ID_2)

        when:
            1 * processRepository.findOneByNameAndActiveIsTrue(destination) >> process

            1 * processRunner.start(process, params) >> processRun

            def actual = processInteractor.send(destination, params)

        then:
            actual == processRun
    }

    def 'Collection<ProcessRun> output(Process source, Map params)'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            Process process1 = new Process(id: ID_1)
            Process process2 = new Process(id: ID_2)
            Process process3 = new Process(id: ID_3)

            Map params = [ key: 'value' ]

            ProcessRun processRun1 = new ProcessRun(id: ID_4)

        when:
            1 * processRepository.findByActiveIsTrue() >> [ process2, process3 ]
            1 * processInteractor.isListeningTo(process1, process2) >> true
            1 * processInteractor.isListeningTo(process1, process3) >> false

            1 * processRunner.start(process2, params) >> processRun1
            0 * processRunner.start(process3, _)

            def actual = processInteractor.output(process1, params)

        then:
            actual == [ processRun1 ]
    }

    def 'Collection<ProcessRun> emit(String event, Map params)'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            Process process1 = new Process(id: ID_1)
            Process process2 = new Process(id: ID_2)
            Process process3 = new Process(id: ID_3)

            String event = 'event'
            Map params = [ key: 'value' ]

            ProcessRun processRun1 = new ProcessRun(id: ID_4)
            ProcessRun processRun2 = new ProcessRun(id: ID_5)

        when:
            1 * processRepository.findByActiveIsTrue() >> [ process1, process2, process3 ]
            1 * processInteractor.isWaitingFor(event, process1) >> false
            1 * processInteractor.isWaitingFor(event, process2) >> true
            1 * processInteractor.isWaitingFor(event, process3) >> true

            0 * processRunner.start(process1, _)
            1 * processRunner.start(process2, params) >> processRun1
            1 * processRunner.start(process3, params) >> processRun2

            def actual = processInteractor.emit(event, params)

        then:
            actual == [ processRun1, processRun2 ]
    }

    def 'ProcessRun repeatOnce(Process process, long afterMillis)'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            Process process = new Process(id: ID_1)

            ProcessRun processRun = new ProcessRun(id: ID_2)

            Date date = new Date()

        when:
            1 * processInteractor.dateAfterMillis(1234) >> date

            1 * processRunner.schedule(process, date) >> processRun

            def actual = processInteractor.repeatOnce(process, 1234)

        then:
            actual == processRun
    }

    def 'boolean isSelfStarting(Process process)'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            Process process = new Process(id: ID_1, definition: 'start 1\nline\nstart 2')
            Binding binding = new Binding()
            binding._IS_SELF_STARTING_ = isSelfStarting

        when:
            1 * groovyBindings.selfStartingBinding(process) >> binding
            1 * processInteractor.evaluate(binding, 'start 1\nstart 2') >> null

            def actual = processInteractor.isSelfStarting(process)

        then:
            actual == isSelfStarting

        where:
            isSelfStarting << [ true, false ]
    }

    def 'boolean isListeningTo(Process source, Process destination)'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            Process source = new Process(id: ID_1)
            Process destination = new Process(id: ID_2, definition: 'listen 1\nline\nlisten 2')
            Binding binding = new Binding()
            binding._IS_LISTENING_ = isListeningTo

        when:
            1 * groovyBindings.isListeningToBinding(source, destination) >> binding
            1 * processInteractor.evaluate(binding, 'listen 1\nlisten 2') >> null

            def actual = processInteractor.isListeningTo(source, destination)

        then:
            actual == isListeningTo

        where:
            isListeningTo << [ true, false ]
    }

    def 'boolean isWaitingFor(String event, Process destination)'() {
        given:
            processInteractor.processRunner = processRunner
            processInteractor.processRepository = processRepository
            processInteractor.groovyBindings = groovyBindings

            String event = 'event'
            Process destination = new Process(id: ID_1, definition: 'waiting 1\nline\nwaiting 2')
            Binding binding = new Binding()
            binding._IS_WAITING_FOR_ = isWaitingFor

        when:
            1 * groovyBindings.isWaitingForBinding(event, destination) >> binding
            1 * processInteractor.evaluate(binding, 'waiting 1\nwaiting 2') >> null

            def actual = processInteractor.isWaitingFor(event, destination)

        then:
            actual == isWaitingFor

        where:
            isWaitingFor << [ true, false ]
    }
}
