package com.github.dipodoc.diploweb.controller.diploexec

import com.github.dipodoc.diploweb.domain.diploexec.Process
import com.github.dipodoc.diploweb.domain.diploexec.ProcessRun
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(ProcessRunController)
@Mock([ Process, ProcessRun ])
class ProcessRunControllerSpec extends Specification {

    @Ignore
    def "'list' action"() {
        given: 'single domain instance'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            ProcessRun processRun = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0).save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.processRunInstanceCount == 1
            model.processRunInstanceList == [ processRun ]
    }

    @Ignore
    def "'list' action with pagination"() {
        given: 'two domain instances'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            ProcessRun processRun1 = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0).save flush:true
            ProcessRun processRun2 = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0).save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.processRunInstanceCount == 2
            model.processRunInstanceList == [ processRun1 ] || model.processRunInstanceList == [ processRun2 ]
    }

    def "'show' action"() {
        when: 'domain instance is passed to the action'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            ProcessRun processRun = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0)
            controller.show(processRun)

        then: 'model contains this instance'
            model.processRunInstance == processRun
    }

    def "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.show(null)

        then: '404 error is returned'
            response.status == 404
    }
}
