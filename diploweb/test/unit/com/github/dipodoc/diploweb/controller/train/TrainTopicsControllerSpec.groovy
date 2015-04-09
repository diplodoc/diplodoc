package com.github.dipodoc.diploweb.controller.train

import com.github.dipodoc.diploweb.domain.diplodata.Post
import com.github.dipodoc.diploweb.domain.diplodata.Topic
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Ignore
import spock.lang.Specification

@TestFor(TrainTopicsController)
@Mock([ Post, Topic ])
class TrainTopicsControllerSpec extends Specification {

    def "'list' action"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Post().save flush:true
            Post post2 = new Post(train_topics: [ topic ]).save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains instance with train_topics field'
            model.postInstanceCount == 1
            model.postInstanceList == [ post2 ]
    }

    def "'list' action with pagination"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Post().save flush:true
            Post post2 = new Post(train_topics: [ topic ]).save flush:true
            Post post3 = new Post(train_topics: [ topic ]).save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains only one instance with train_meaningHtml field, total instances count is 2'
            model.postInstanceCount == 2
            model.postInstanceList == [ post2 ] || model.postInstanceList == [ post3 ]
    }

    @Ignore
    def "'trainNext' action with no 'id' parameter"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Post().save flush:true
            new Post(train_topics: []).save flush:true
            new Post(train_topics: [ topic ]).save flush:true

        when: 'action is executed'
            def model = controller.trainNext()

        then: 'model contains instance without train_meaningHtml field'
            model.postToTrain != null
            model.postToTrain.train_topics == null || model.postToTrain.train_topics == []
    }

    def "'trainNext' action with 'id' parameter"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true
            new Post().save flush:true
            new Post(train_topics: []).save flush:true
            Post post = new Post(train_topics: [ topic ]).save flush:true

        when: 'action is executed'
            params.id = post.id
            def model = controller.trainNext()

        then: 'model contains instance without train_meaningHtml field'
            model.postToTrain == post
    }

    def "'edit' action"() {
        when: 'action is executed'
            Post post = new Post(id: new ObjectId('111111111111111111111111')).save flush:true
            controller.edit(post)

        then: 'model is populated with domain instance'
            model.postInstance == post
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
            Post post = new Post(train_topics: [ topic ]).save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.id = post.id
            controller.removeFromTrain()

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == '/trainTopics/list'
            Post.get(post.id).train_topics.isEmpty()
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

    @Ignore
    def "'removeTopicFromTrainingSet' action with valid domain instance"() {
        given: 'domain instances'
            Topic topic1 = new Topic(label: 'label').save flush:true
            Topic topic2 = new Topic(label: 'label').save flush:true
            Post post = new Post(train_topics: [ topic1, topic2 ]).save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.postId = post.id
            params.topicId = topic1.id
            params.redirectTo = 'redirect'
            controller.removeTopicFromTrainingSet()

        then: "topic is removed from train list, redirect is issued to the 'redirect' action"
            response.redirectedUrl == "/trainTopics/redirect/$post.id"
            params.id == post.id
            Post.get(post.id).train_topics == [ topic2 ]
    }

    void "'removeTopicFromTrainingSet' action with null domain"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.postId = 0
            params.topicId = topic.id
            params.redirectTo = 'redirect'
            controller.removeTopicFromTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }

    void "'removeTopicFromTrainingSet' action with invalid topic"() {
        given: 'domain instances'
            Post post = new Post(train_topics: []).save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.postId = post.id
            params.topicId = 0
            params.redirectTo = 'redirect'
            controller.removeTopicFromTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }

    @Ignore
    def "'addTopicToTrainingSet' action with valid domain instance"() {
        given: 'domain instances'
            Topic topic1 = new Topic(label: 'label').save flush:true
            Topic topic2 = new Topic(label: 'label').save flush:true
            Post post = new Post(train_topics: [ topic1 ]).save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.postId = post.id
            params.topicId = topic2.id
            params.redirectTo = 'redirect'
            controller.addTopicToTrainingSet()

        then: "topic is added from train list, redirect is issued to the 'redirect' action"
            response.redirectedUrl == "/trainTopics/redirect/$post.id"
            params.id == post.id
            Post.get(post.id).train_topics == [ topic1, topic2 ]
    }

    @Ignore
    void "'addTopicToTrainingSet' action with null domain"() {
        given: 'domain instances'
            Topic topic = new Topic(label: 'label').save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.postId = 0
            params.topicId = topic.id
            params.redirectTo = 'redirect'
            controller.addTopicToTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }

    @Ignore
    void "'addTopicToTrainingSet' action with invalid topic"() {
        given: 'domain instances'
            Post post = new Post(train_topics: []).save flush:true

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.postId = post.id
            params.topicId = 0
            params.redirectTo = 'redirect'
            controller.addTopicToTrainingSet()

        then: '404 error is returned'
            response.redirectedUrl == '/trainTopics/list'
            flash.message != null
    }
}
