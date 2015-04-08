package com.github.dipodoc.diploweb.controller.diplodata

import com.github.dipodoc.diploweb.domain.diplodata.Post
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(PostController)
@Mock(Post)
class PostControllerSpec extends Specification {

    def "'list' action"() {
        given: 'single Post instance'
            Post post = new Post().save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.postInstanceCount == 1
            model.postInstanceList == [ post ]
    }

    def "'list' action with pagination"() {
        given: 'two Post instances'
            Post post1 = new Post().save flush:true
            Post post2 = new Post().save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.postInstanceCount == 2
            model.postInstanceList == [ post1 ] || model.postInstanceList == [ post2 ]
    }

    void "'show' action"() {
        when: 'domain instance is passed to the action'
            Post post = new Post(id: new ObjectId('111111111111111111111111'))
            controller.show(post)

        then: 'model contains this instance'
            model.postInstance == post
    }

    void "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.show(null)

        then: 'A 404 error is returned'
            response.status == 404
    }

    void "'delete' action"() {
        when: 'domain instance is created'
            Post post = new Post().save flush:true

        then: 'it exists'
            Post.count() == 1

        when: 'action is called'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(post)

        then: 'instance is deleted, correct response is returned'
            Post.count() == 0
            response.redirectedUrl == '/post/list'
            flash.message != null
    }

    void "'delete' action with null domain"() {
        when: 'action is called for a null instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "redirect to 'list' action"
            response.redirectedUrl == '/post/list'
            flash.message != null
    }
}
