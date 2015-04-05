package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Post
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(DiploclientController)
@Mock(Post)
class DiploclientControllerSpec extends Specification {

    def "'postList' action with sorting"() {
        given: 'two posts'
            Post post1 = new Post(publishTime: '1').save flush:true
            Post post2 = new Post(publishTime: '2').save flush:true

        when: 'action is executed'
            controller.postList()

        then: 'model contains posts sorted by publishTime desc'
            model.postInstanceCount == 2
            model.postInstanceList == [ post2, post1 ]
    }

    void "'show' action"() {
        when: 'domain instance is passed to the action'
            Post post = new Post(id: new ObjectId('111111111111111111111111'))
            controller.postShow(post)

        then: 'model contains this instance'
            model.postInstance == post
    }

    void "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.postShow(null)

        then: '404 error is returned'
            response.status == 404
    }
}
