package com.github.dipodoc.webui.admin.controller.orchestration
import com.github.dipodoc.webui.admin.domain.orchestration.Module
import com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethod
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ModuleMethodController)
@Mock([ Module, ModuleMethod ])
class ModuleMethodControllerSpec extends Specification {

    def "'create' action"() {
        when: 'action is executed'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            params.moduleId = module.id

            controller.create()

        then: 'model is correctly created, module is populated'
            model.moduleMethodInstance != null
            model.moduleMethodInstance.module == module
    }

    def "'save' action with valid domain instance"() {
        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            ModuleMethod moduleMethod = new ModuleMethod(name: 'name', module: module)

            controller.save(moduleMethod)

        then: "redirect is issued to the 'module/show' action"
            response.redirectedUrl == "/module/show/$module.id"
            ModuleMethod.count() == 1
    }

    def "'save' action with invalid domain instance"() {
        when: 'action is executed with an invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def moduleMethod = new ModuleMethod()
            moduleMethod.validate()
            controller.save(moduleMethod)

        then: "'create' view is rendered again with the correct model"
            model.moduleMethodInstance != null
            view == 'create'
    }

    def "'edit' action"() {
        when: 'action is executed'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            ModuleMethod moduleMethod = new ModuleMethod(name: 'name', module: module)
            controller.edit(moduleMethod)

        then: 'model is populated with domain instance'
            model.moduleMethodInstance == moduleMethod
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
            ModuleMethod moduleMethod = new ModuleMethod(name: 'name', module: module)
            controller.update(moduleMethod)

        then: "redirect is issues to the 'show' action"
            response.redirectedUrl == "/module/show/$module.id"
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
            def moduleMethod = new ModuleMethod()
            moduleMethod.validate()
            controller.update(moduleMethod)

        then: "'edit' view is rendered again with the invalid instance"
            view == 'edit'
            model.moduleMethodInstance == moduleMethod
    }

    void "'delete' action"() {
        when: 'domain instance is created'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            ModuleMethod moduleMethod = new ModuleMethod(name: 'name', module: module).save flush:true

        then: 'it exists'
            ModuleMethod.count() == 1

        when: 'action is called'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(moduleMethod)

        then: 'instance is deleted, correct response is returned'
            ModuleMethod.count() == 0
            response.redirectedUrl == "/module/show/$module.id"
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
