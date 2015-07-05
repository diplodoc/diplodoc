package com.github.dipodoc.webui.admin.controller.data

import com.github.dipodoc.webui.admin.domain.data.Doc
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(DocController)
@Mock(Doc)
class DocControllerSpec extends Specification {

    def "'list' action"() {
        given: 'single Doc instance'
            Doc doc = new Doc().save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains this single instance'
            model.docCount == 1
            model.docList == [ doc ]
    }

    def "'list' action with pagination"() {
        given: 'two Doc instances'
            Doc doc1 = new Doc().save flush:true
            Doc doc2 = new Doc().save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.docCount == 2
            model.docList == [ doc1 ] || model.docList == [ doc2 ]
    }

    void "'show' action"() {
        when: 'domain instance is passed to the action'
            Doc doc = new Doc(id: new ObjectId('111111111111111111111111'))
            controller.show(doc)

        then: 'model contains this instance'
            model.doc == doc
    }

    void "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.show(null)

        then: 'A 404 error is returned'
            response.status == 404
    }

    void "'delete' action"() {
        when: 'domain instance is created'
            Doc doc = new Doc().save flush:true

        then: 'it exists'
            Doc.count() == 1

        when: 'action is called'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(doc)

        then: 'instance is deleted, correct response is returned'
            Doc.count() == 0
            response.redirectedUrl == '/doc/list'
            flash.message != null
    }

    void "'delete' action with null domain"() {
        when: 'action is called for a null instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then: "redirect to 'list' action"
            response.redirectedUrl == '/doc/list'
            flash.message != null
    }
}
