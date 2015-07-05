package com.github.dipodoc.webui.admin.controller.orchestration

import com.github.dipodoc.webui.admin.domain.orchestration.Process
import com.github.dipodoc.webui.admin.domain.orchestration.ProcessRun
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ProcessRunController)
@Mock([ Process, ProcessRun ])
class ProcessRunControllerSpec extends Specification {

    def "'list' action"() {
        given: 'single domain instance'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            ProcessRun processRun = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0).save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.processRunCount == 1
            model.processRunList == [ processRun ]
    }

    def "'list' action with pagination"() {
        given: 'two domain instances'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            ProcessRun processRun1 = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0).save flush:true
            ProcessRun processRun2 = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0).save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.processRunCount == 2
            model.processRunList == [ processRun1 ] || model.processRunList == [ processRun2 ]
    }

    def "'show' action"() {
        when: 'domain instance is passed to the action'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            ProcessRun processRun = new ProcessRun(process: process, parameters: [], startTime: '1', endTime: '2', exitStatus: 0)
            controller.show(processRun)

        then: 'model contains this instance'
            model.processRun == processRun
    }

    def "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.show(null)

        then: '404 error is returned'
            response.status == 404
    }
}
