package com.github.dipodoc.diploweb.controller.train

import com.github.dipodoc.diploweb.domain.diplodata.Post
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(TrainMeaningHtmlController)
@Mock(Post)
class TrainMeaningHtmlControllerSpec extends Specification {

    def "'list' action"() {
        given: 'domain instances'
            new Post().save flush:true
            Post post2 = new Post(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains instance with train_meaningHtml field'
            model.postInstanceCount == 1
            model.postInstanceList == [ post2 ]
    }

    def "'list' action with pagination"() {
        given: 'domain instances'
            new Post().save flush:true
            Post post2 = new Post(train_meaningHtml: '<html/>').save flush:true
            Post post3 = new Post(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains only one instance with train_meaningHtml field, total instances count is 2'
            model.postInstanceCount == 2
            model.postInstanceList == [ post2 ] || model.postInstanceList == [ post3 ]
    }

    def "'trainNext' action"() {
        given: 'domain instances'
            new Post().save flush:true
            new Post(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed'
            def model = controller.trainNext()

        then: 'model contains instance without train_meaningHtml field'
            model.postToTrain != null
            model.postToTrain.train_meaningHtml == null
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

    def "'saveAndNext' action with valid domain instance"() {
        given: 'domain instance'
            Post post = new Post().save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.id = post.id
            params.train_meaningHtml = '<html/>'
            controller.saveAndNext()

        then: "redirect is issued to the '/trainMeaningHtml/trainNext' action"
            response.redirectedUrl == '/trainMeaningHtml/trainNext'
            Post.get(post.id).train_meaningHtml == '<html/>'
    }

    def "'saveAndNext' action with invalid domain instance"() {
        given: 'no domain instances'

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.id = 0
            params.train_meaningHtml = '<html/>'
            controller.saveAndNext()

        then: '404 error is returned'
            response.redirectedUrl == '/trainMeaningHtml/list'
    }

    def "'save' action with valid domain instance"() {
        given: 'domain instance'
            Post post = new Post().save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.id = post.id
            params.train_meaningHtml = '<html/>'
            controller.save()

        then: "redirect is issued to the '/trainMeaningHtml/list' action"
            response.redirectedUrl == '/trainMeaningHtml/list'
            Post.get(post.id).train_meaningHtml == '<html/>'
    }

    def "'save' action with invalid domain instance"() {
        given: 'no domain instances'

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.id = 0
            params.train_meaningHtml = '<html/>'
            controller.save()

        then: '404 error is returned'
            response.redirectedUrl == '/trainMeaningHtml/list'
    }

    def "'removeFromTrain' action with valid domain instance"() {
        given: 'domain instance'
            Post post = new Post(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.id = post.id
            controller.removeFromTrain()

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == '/trainMeaningHtml/list'
            Post.get(post.id).train_meaningHtml == null
    }

    void "'removeFromTrain' action with null domain"() {
        given: 'no domain instances'

        when: 'action is executed with a invalid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.id = 0
            controller.removeFromTrain()

        then: '404 error is returned'
            response.redirectedUrl == '/trainMeaningHtml/list'
            flash.message != null
    }
}
