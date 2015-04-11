package com.github.dipodoc.diploweb.controller

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(DiploclientController)
@Mock(Doc)
class DiploclientControllerSpec extends Specification {

    def "'docList' action with sorting"() {
        given: 'two docs'
            Doc doc1 = new Doc(publishTime: '1').save flush:true
            Doc doc2 = new Doc(publishTime: '2').save flush:true

        when: 'action is executed'
            controller.docList()

        then: 'model contains docs sorted by publishTime desc'
            model.docInstanceCount == 2
            model.docInstanceList == [ doc2, doc1 ]
    }

    void "'show' action"() {
        when: 'domain instance is passed to the action'
            Doc doc = new Doc(id: new ObjectId('111111111111111111111111'))
            controller.docShow(doc)

        then: 'model contains this instance'
            model.docInstance == doc
    }

    void "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.docShow(null)

        then: '404 error is returned'
            response.status == 404
    }
}
