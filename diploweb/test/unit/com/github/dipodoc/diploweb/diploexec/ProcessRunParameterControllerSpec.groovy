package com.github.dipodoc.diploweb.diploexec



import grails.test.mixin.*
import spock.lang.*

@TestFor(ProcessRunParameterController)
@Mock(ProcessRunParameter)
class ProcessRunParameterControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.processRunParameterInstanceList
            model.processRunParameterInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.processRunParameterInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def processRunParameter = new ProcessRunParameter()
            processRunParameter.validate()
            controller.save(processRunParameter)

        then:"The create view is rendered again with the correct model"
            model.processRunParameterInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            processRunParameter = new ProcessRunParameter(params)

            controller.save(processRunParameter)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/processRunParameter/show/1'
            controller.flash.message != null
            ProcessRunParameter.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def processRunParameter = new ProcessRunParameter(params)
            controller.show(processRunParameter)

        then:"A model is populated containing the domain instance"
            model.processRunParameterInstance == processRunParameter
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def processRunParameter = new ProcessRunParameter(params)
            controller.edit(processRunParameter)

        then:"A model is populated containing the domain instance"
            model.processRunParameterInstance == processRunParameter
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/processRunParameter/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def processRunParameter = new ProcessRunParameter()
            processRunParameter.validate()
            controller.update(processRunParameter)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.processRunParameterInstance == processRunParameter

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            processRunParameter = new ProcessRunParameter(params).save(flush: true)
            controller.update(processRunParameter)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/processRunParameter/show/$processRunParameter.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/processRunParameter/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def processRunParameter = new ProcessRunParameter(params).save(flush: true)

        then:"It exists"
            ProcessRunParameter.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(processRunParameter)

        then:"The instance is deleted"
            ProcessRunParameter.count() == 0
            response.redirectedUrl == '/processRunParameter/index'
            flash.message != null
    }
}
