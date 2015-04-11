package com.github.dipodoc.diploweb.controller.train

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(TrainMeaningHtmlController)
@Mock(Doc)
class TrainMeaningHtmlControllerSpec extends Specification {

    def "'list' action"() {
        given: 'domain instances'
            new Doc().save flush:true
            Doc doc2 = new Doc(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains instance with train_meaningHtml field'
            model.docInstanceCount == 1
            model.docInstanceList == [ doc2 ]
    }

    def "'list' action with pagination"() {
        given: 'domain instances'
            new Doc().save flush:true
            Doc doc2 = new Doc(train_meaningHtml: '<html/>').save flush:true
            Doc doc3 = new Doc(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains only one instance with train_meaningHtml field, total instances count is 2'
            model.docInstanceCount == 2
            model.docInstanceList == [ doc2 ] || model.docInstanceList == [ doc3 ]
    }

    def "'trainNext' action"() {
        given: 'domain instances'
            new Doc().save flush:true
            new Doc(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed'
            def model = controller.trainNext()

        then: 'model contains instance without train_meaningHtml field'
            model.docToTrain != null
            model.docToTrain.train_meaningHtml == null
    }

    def "'edit' action"() {
        when: 'action is executed'
            Doc doc = new Doc(id: new ObjectId('111111111111111111111111')).save flush:true
            controller.edit(doc)

        then: 'model is populated with domain instance'
            model.docInstance == doc
    }

    def "'edit' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.edit(null)

        then: '404 error is returned'
            response.status == 404
    }

    def "'saveAndNext' action with valid domain instance"() {
        given: 'domain instance'
            Doc doc = new Doc().save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.id = doc.id
            params.train_meaningHtml = '<html/>'
            controller.saveAndNext()

        then: "redirect is issued to the '/trainMeaningHtml/trainNext' action"
            response.redirectedUrl == '/trainMeaningHtml/trainNext'
            Doc.get(doc.id).train_meaningHtml == '<html/>'
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
            Doc doc = new Doc().save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            params.id = doc.id
            params.train_meaningHtml = '<html/>'
            controller.save()

        then: "redirect is issued to the '/trainMeaningHtml/list' action"
            response.redirectedUrl == '/trainMeaningHtml/list'
            Doc.get(doc.id).train_meaningHtml == '<html/>'
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
            Doc doc = new Doc(train_meaningHtml: '<html/>').save flush:true

        when: 'action is executed with a valid instance'
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            params.id = doc.id
            controller.removeFromTrain()

        then: "redirect is issued to the 'show' action"
            response.redirectedUrl == '/trainMeaningHtml/list'
            Doc.get(doc.id).train_meaningHtml == null
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
