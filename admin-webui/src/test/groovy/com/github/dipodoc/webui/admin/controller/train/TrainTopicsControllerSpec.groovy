package com.github.dipodoc.webui.admin.controller.train

import com.github.dipodoc.webui.admin.domain.data.Doc
import com.github.dipodoc.webui.admin.domain.data.Topic
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(TrainTopicsController)
@Mock([ Doc, Topic ])
class TrainTopicsControllerSpec extends Specification {

    def "'list' action"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Doc().save flush:true
            Doc doc2 = new Doc(train_topics: [ topic ]).save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains instance with train_topics field'
            model.docCount == 1
            model.docList == [ doc2 ]
    }

    def "'list' action with pagination"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Doc().save flush:true
            Doc doc2 = new Doc(train_topics: [ topic ]).save flush:true
            Doc doc3 = new Doc(train_topics: [ topic ]).save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains only one instance with train_meaningHtml field, total instances count is 2'
            model.docCount == 2
            model.docList == [ doc2 ] || model.docList == [ doc3 ]
    }

    @Ignore('FIXIT: DIPLODOC-145. Fix multiple failing tests in diploweb')
    def "'trainNext' action with no 'id' parameter"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Doc().save flush:true
            new Doc(train_topics: []).save flush:true
            new Doc(train_topics: [ topic ]).save flush:true

        when: 'action is executed'
            def model = controller.trainNext()

        then: 'model contains instance without train_meaningHtml field'
            model.docToTrain != null
            model.docToTrain.train_topics == null || model.docToTrain.train_topics == []
    }

    def "'trainNext' action with 'id' parameter"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Doc().save flush:true
            new Doc(train_topics: []).save flush:true
            Doc doc = new Doc(train_topics: [ topic ]).save flush:true

        when: 'action is executed'
            params.id = doc.id
            def model = controller.trainNext()

        then: 'model contains instance without train_meaningHtml field'
            model.docToTrain == doc
    }

    def "'edit' action"() {
        when: 'action is executed'
            Doc doc = new Doc(id: new ObjectId('111111111111111111111111')).save flush:true
            controller.edit(doc)

        then: 'model is populated with domain instance'
            model.doc == doc
    }

    def "'edit' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.edit(null)

        then: '404 error is returned'
            response.status == 404
    }

    def "'removeFromTrain' action with valid domain instance"() {
        given: 'domain instance'
            Topic topic = new Topic(label: 'label').save flush:true
            Doc doc = new Doc(train_topics: [ topic ]).save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.id = doc.id
            controller.removeFromTrain()

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == '/trainTopics/list'
            Doc.get(doc.id).train_topics.isEmpty()
    }

    void "'removeFromTrain' action with null domain"() {
        given: 'no domain instances'

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.id = 0
            controller.removeFromTrain()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }

    @Ignore('FIXIT: DIPLODOC-145. Fix multiple failing tests in diploweb')
    def "'removeTopicFromTrainingSet' action with valid domain instance"() {
        given: 'domain instances'
            Topic topic1 = new Topic(label: 'label').save flush:true
            Topic topic2 = new Topic(label: 'label').save flush:true
            Doc doc = new Doc(train_topics: [ topic1, topic2 ]).save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.docId = doc.id
            params.topicId = topic1.id
            params.redirectTo = 'redirect'
            controller.removeTopicFromTrainingSet()

        then: "topic is removed from train list, redirect is issued to the 'redirect' action"
            response.redirectedUrl == "/trainTopics/redirect/$doc.id"
            params.id == doc.id
            Doc.get(doc.id).train_topics == [ topic2 ]
    }

    void "'removeTopicFromTrainingSet' action with null domain"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.docId = 0
            params.topicId = topic.id
            params.redirectTo = 'redirect'
            controller.removeTopicFromTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }

    void "'removeTopicFromTrainingSet' action with invalid topic"() {
        given: 'domain instances'
            Doc doc = new Doc(train_topics: []).save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.docId = doc.id
            params.topicId = 0
            params.redirectTo = 'redirect'
            controller.removeTopicFromTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }

    @Ignore('FIXIT: DIPLODOC-145. Fix multiple failing tests in diploweb')
    def "'addTopicToTrainingSet' action with valid domain instance"() {
        given: 'domain instances'
            Topic topic1 = new Topic(label: 'label').save flush:true
            Topic topic2 = new Topic(label: 'label').save flush:true
            Doc doc = new Doc(train_topics: [ topic1 ]).save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.docId = doc.id
            params.topicId = topic2.id
            params.redirectTo = 'redirect'
            controller.addTopicToTrainingSet()

        then: "topic is added from train list, redirect is issued to the 'redirect' action"
            response.redirectedUrl == "/trainTopics/redirect/$doc.id"
            params.id == doc.id
            Doc.get(doc.id).train_topics == [ topic1, topic2 ]
    }

    @Ignore('FIXIT: DIPLODOC-145. Fix multiple failing tests in diploweb')
    void "'addTopicToTrainingSet' action with null domain"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.docId = 0
            params.topicId = topic.id
            params.redirectTo = 'redirect'
            controller.addTopicToTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }

    @Ignore('FIXIT: DIPLODOC-145. Fix multiple failing tests in diploweb')
    void "'addTopicToTrainingSet' action with invalid topic"() {
        given: 'domain instances'
            Doc doc = new Doc(train_topics: []).save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.docId = doc.id
            params.topicId = 0
            params.redirectTo = 'redirect'
            controller.addTopicToTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }
}
