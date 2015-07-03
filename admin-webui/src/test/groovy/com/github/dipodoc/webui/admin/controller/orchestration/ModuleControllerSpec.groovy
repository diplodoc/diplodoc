package com.github.dipodoc.webui.admin.controller.orchestration

import com.github.dipodoc.webui.admin.domain.orchestration.Module
import com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethod
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(ModuleController)
@Mock([ Module, ModuleMethod ])
class ModuleControllerSpec extends Specification {

    def "'list' action"() {
        given: 'single domain instance'
            Module module = new Module(name: 'name', data: [:]).save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.moduleInstanceCount == 1
            model.moduleInstanceList == [ module ]
    }

    def "'list' action with pagination"() {
        given: 'two domain instances'
            Module module1 = new Module(name: 'name', data: [:]).save flush:true
            Module module2 = new Module(name: 'name', data: [:]).save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.moduleInstanceCount == 2
            model.moduleInstanceList == [ module1 ] || model.topicInstanceList == [ module2 ]
    }

    def "'show' action"() {
        given: 'domain instances'
            Module module1 = new Module(id: new ObjectId('111111111111111111111111'), name: 'module1', data: [:])
            Module module2 = new Module(id: new ObjectId('222222222222222222222222'), name: 'module2', data: [:])
            ModuleMethod moduleMethod1 = new ModuleMethod(module: module1, name: 'moduleMethod1').save flush:true
            new ModuleMethod(module: module2, name: 'moduleMethod2').save flush:true

        when: 'domain instance is passed to the action'
            def model = controller.show(module1)

        then: 'model contains this instance'
            model.moduleInstance == module1
            model.moduleMethodsList == [ moduleMethod1 ]
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
            model.moduleInstance != null
    }

    def "'save' action with valid domain instance"() {
        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            Module module = new Module(name: 'name', data: [:])

            controller.save(module)

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == "/module/show/$module.id"
            controller.flash.message != null
            Module.count() == 1
    }

    def "'save' action with invalid domain instance"() {
        when: 'action is executed with an invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def module = new Module()
            module.validate()
            controller.save(module)

        then: "'create' view is rendered again with the correct model"
            model.moduleInstance != null
            view == 'create'
    }

    def "'edit' action"() {
        given: 'domain instances'
            Module module1 = new Module(id: new ObjectId('111111111111111111111111'), name: 'module1', data: [:])
            Module module2 = new Module(id: new ObjectId('222222222222222222222222'), name: 'module2', data: [:])
            ModuleMethod moduleMethod1 = new ModuleMethod(module: module1, name: 'moduleMethod1').save flush:true
            new ModuleMethod(module: module2, name: 'moduleMethod2').save flush:true

        when: 'action is executed'
            def model = controller.edit(module1)

        then: 'model is populated with domain instance'
            model.moduleInstance == module1
            model.moduleMethodsList == [ moduleMethod1 ]
    }

    def "'edit' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.edit(null)

        then: '404 error is returned'
            response.status == 404
    }

    def "'update' action with valid domain instance"() {
        when: 'valid domain instance is passed to the action'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            controller.update(module)

        then: "redirect is issues to the 'show' action"
            response.redirectedUrl == "/module/show/$module.id"
            flash.message != null
    }

    def "'update' action with null domain"() {
        when: 'action is called for null'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: '404 error is returned'
            response.redirectedUrl == '/module/list'
            flash.message != null
    }

    def "'update' action with invalid domain instance"() {
        when: 'invalid domain instance is passed to the action'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            def module = new Module()
            module.validate()
            controller.update(module)

        then: "'edit' view is rendered again with the invalid instance"
            view == 'edit'
            model.moduleInstance == module
    }

    void "'delete' action"() {
        when: 'domain instance is created'
            Module module = new Module(name: 'name', data: [:]).save flush:true

        then: 'it exists'
            Module.count() == 1

        when: 'action is called'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(module)

        then: 'instance is deleted, correct response is returned'
            Module.count() == 0
            response.redirectedUrl == '/module/list'
            flash.message != null
    }

    void "'delete' action with null domain"() {
        when: 'action is called for a null instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "redirect to 'list' action"
            response.redirectedUrl == '/module/list'
            flash.message != null
    }
}
