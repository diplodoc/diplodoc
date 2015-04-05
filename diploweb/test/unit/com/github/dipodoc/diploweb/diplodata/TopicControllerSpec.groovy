package com.github.dipodoc.diploweb.diplodata

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(TopicController)
@Mock(Topic)
class TopicControllerSpec extends Specification {

    def "'list' action"() {
        given: 'single domain instance'
            Topic topic = new Topic(label: 'label').save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.topicInstanceCount == 1
            model.topicInstanceList == [ topic ]
    }

    def "'list' action with pagination"() {
        given: 'two domain instances'
            Topic topic1 = new Topic(label: 'label').save flush:true
            Topic topic2 = new Topic(label: 'label').save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.topicInstanceCount == 2
            model.topicInstanceList == [ topic1 ] || model.topicInstanceList == [ topic2 ]
    }

    def "'show' action"() {
        when: 'domain instance is passed to the action'
            Topic topic = new Topic(id: new ObjectId('111111111111111111111111'), label: 'label')
            controller.show(topic)

        then: 'model contains this instance'
            model.topicInstance == topic
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
            model.topicInstance != null
    }

    def "'save' action with valid domain instance"() {
        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            Topic topic = new Topic(id: new ObjectId('111111111111111111111111'), label: 'label')

            controller.save(topic)

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == "/topic/show/$topic.id"
            controller.flash.message != null
            Topic.count() == 1
    }

    def "'save' action with invalid domain instance"() {
        when: 'action is executed with an invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def topic = new Topic()
            topic.validate()
            controller.save(topic)

        then: "'create' view is rendered again with the correct model"
            model.topicInstance != null
            view == 'create'
    }

    def "'edit' action"() {
        when: 'action is executed'
            Topic topic = new Topic(id: new ObjectId('111111111111111111111111'), label: 'label')
            controller.edit(topic)

        then: 'model is populated with domain instance'
            model.topicInstance == topic
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
            Topic topic = new Topic(label: 'label').save(flush: true)
            controller.update(topic)

        then: "redirect is issues to the 'show' action"
            response.redirectedUrl == "/topic/show/$topic.id"
            flash.message != null
    }

    def "'update' action with null domain"() {
        when: 'action is called for null'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then: '404 error is returned'
            response.redirectedUrl == '/topic/list'
            flash.message != null
    }

    def "'update' action with invalid domain instance"() {
        when: 'invalid domain instance is passed to the action'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            def topic = new Topic()
            topic.validate()
            controller.update(topic)

        then: "'edit' view is rendered again with the invalid instance"
            view == 'edit'
            model.topicInstance == topic
    }

    void "'delete' action"() {
        when: 'domain instance is created'
            Topic topic = new Topic(label: 'label').save(flush: true)

        then: 'it exists'
            Topic.count() == 1

        when: 'action is called'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(topic)

        then: 'instance is deleted, correct response is returned'
            Topic.count() == 0
            response.redirectedUrl == '/topic/list'
            flash.message != null
    }

    void "'delete' action with null domain"() {
        when: 'action is called for a null instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "redirect to 'list' action"
            response.redirectedUrl == '/topic/list'
            flash.message != null
    }
}
