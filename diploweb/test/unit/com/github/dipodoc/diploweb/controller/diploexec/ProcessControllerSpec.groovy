package com.github.dipodoc.diploweb.controller.diploexec

import com.github.dipodoc.diploweb.domain.diploexec.Process
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(ProcessController)
@Mock(Process)
class ProcessControllerSpec extends Specification {

    @Ignore
    def "'list' action"() {
        given: 'single domain instance'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.processInstanceCount == 1
            model.processInstanceList == [ process ]
    }

    @Ignore
    def "'list' action with pagination"() {
        given: 'two domain instances'
            Process process1 = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            Process process2 = new Process(name: 'name', definition: 'definition', active: true).save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.processInstanceCount == 2
            model.processInstanceList == [ process1 ] || model.processInstanceList == [ process2 ]
    }

    def "'show' action"() {
        when: 'domain instance is passed to the action'
            Process process = new Process(id: 1, name: 'name', definition: 'definition', active: true)
            controller.show(process)

        then: 'model contains this instance'
            model.processInstance == process
    }

    def "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.show(null)

        then: 'A 404 error is returned'
            response.status == 404
    }

    def "'create' action"() {
        when: 'action is executed'
            controller.create()

        then: 'model is correctly created'
            model.processInstance != null
    }

    @Ignore
    def "'save' action with valid domain instance"() {
        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            Process process = new Process(name: 'name', definition: 'definition', active: true)

            controller.save(process)

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == "/process/show/$process.id"
            controller.flash.message != null
            Process.count() == 1
    }

    def "'save' action with invalid domain instance"() {
        when: 'action is executed with an invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def process = new Process()
            process.validate()
            controller.save(process)

        then: "'create' view is rendered again with the correct model"
            model.processInstance != null
            view == 'create'
    }

    def "'edit' action"() {
        when: 'action is executed'
            Process process = new Process(id: 1, name: 'name', definition: 'definition', active: true)
            controller.edit(process)

        then: 'model is populated with domain instance'
            model.processInstance == process
    }

    def "'edit' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.edit(null)

        then: '404 error is returned'
            response.status == 404
    }

    @Ignore
    def "'update' action with valid domain instance"() {
        when: 'valid domain instance is passed to the action'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true
            controller.update(process)

        then: "redirect is issues to the 'show' action"
            response.redirectedUrl == "/process/show/$process.id"
            flash.message != null
    }

    def "'update' action with null domain"() {
        when: 'action is called for null'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: '404 error is returned'
            response.redirectedUrl == '/process/list'
            flash.message != null
    }

    def "'update' action with invalid domain instance"() {
        when: 'invalid domain instance is passed to the action'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            def process = new Process()
            process.validate()
            controller.update(process)

        then: "'edit' view is rendered again with the invalid instance"
            view == 'edit'
            model.processInstance == process
    }

    @Ignore
    void "'delete' action"() {
        when: 'domain instance is created'
            Process process = new Process(name: 'name', definition: 'definition', active: true).save flush:true

        then: 'it exists'
            Process.count() == 1

        when: 'action is called'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(process)

        then: 'instance is deleted, correct response is returned'
            Process.count() == 0
            response.redirectedUrl == '/process/list'
            flash.message != null
    }

    void "'delete' action with null domain"() {
        when: 'action is called for a null instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "redirect to 'list' action"
            response.redirectedUrl == '/process/list'
            flash.message != null
    }
}
