package com.github.dipodoc.webui.admin.controller.data

import com.github.dipodoc.webui.admin.domain.data.Source
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(SourceController)
@Mock(Source)
class SourceControllerSpec extends Specification {

    def "'list' action"() {
        given: 'single domain instance'
            Source source = new Source(name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module').save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.sourceInstanceCount == 1
            model.sourceInstanceList == [ source ]
    }

    def "'list' action with pagination"() {
        given: 'two domain instances'
            Source source1 = new Source(name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module').save flush:true
            Source source2 = new Source(name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module').save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.sourceInstanceCount == 2
            model.sourceInstanceList == [ source1 ] || model.sourceInstanceList == [ source2 ]
    }

    def "'show' action"() {
        when: 'domain instance is passed to the action'
            Source source = new Source(id: new ObjectId('111111111111111111111111'), name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module')
            controller.show(source)

        then: 'model contains this instance'
            model.sourceInstance == source
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
            model.sourceInstance != null
    }

    def "'save' action with valid domain instance"() {
        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            Source source = new Source(name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module')

            controller.save(source)

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == "/source/show/$source.id"
            controller.flash.message != null
            Source.count() == 1
    }

    def "'save' action with invalid domain instance"() {
        when: 'action is executed with an invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def source = new Source()
            source.validate()
            controller.save(source)

        then: "'create' view is rendered again with the correct model"
            model.sourceInstance != null
            view == 'create'
    }

    def "'edit' action"() {
        when: 'action is executed'
            Source source = new Source(id: new ObjectId('111111111111111111111111'), name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module')
            controller.edit(source)

        then: 'model is populated with domain instance'
            model.sourceInstance == source
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
            Source source = new Source(name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module').save(flush: true)
            controller.update(source)

        then: "redirect is issues to the 'show' action"
            response.redirectedUrl == "/source/show/$source.id"
            flash.message != null
    }

    def "'update' action with null domain"() {
        when: 'action is called for null'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: '404 error is returned'
            response.redirectedUrl == '/source/list'
            flash.message != null
    }

    def "'update' action with invalid domain instance"() {
        when: 'invalid domain instance is passed to the action'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            def source = new Source()
            source.validate()
            controller.update(source)

        then: "'edit' view is rendered again with the invalid instance"
            view == 'edit'
            model.sourceInstance == source
    }

    void "'delete' action"() {
        when: 'domain instance is created'
            Source source = new Source(name: 'name', rssUrl: 'rss-url', newDocsFinderModule: 'module').save(flush: true)

        then: 'it exists'
            Source.count() == 1

        when: 'action is called'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(source)

        then: 'instance is deleted, correct response is returned'
            Source.count() == 0
            response.redirectedUrl == '/source/list'
            flash.message != null
    }

    void "'delete' action with null domain"() {
        when: 'action is called for a null instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "redirect to 'list' action"
            response.redirectedUrl == '/source/list'
            flash.message != null
    }
}
