package com.github.dipodoc.webui.admin.controller.train

import com.github.dipodoc.webui.admin.domain.data.Doc
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
class TrainMeaningHtmlController {

    static allowedMethods = [ save: 'PUT', saveAndNext: 'PUT', removeFromTrain: 'DELETE' ]

    Random random = new Random()

    def list(Integer max) {
        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        params.max = Math.min(max ?: 10, 100)
        def trainSet = Doc.findAllByTrain_meaningHtmlIsNotNull(params)

        respond trainSet, model: [ docInstanceCount: Doc.countByTrain_meaningHtmlIsNotNull() ]
    }

    def trainNext() {
        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        int index = random.nextInt(Doc.countByTrain_meaningHtmlIsNull())
        def params = [ offset: index, max: 1 ]

        Doc randomUntrainedDoc = Doc.findByTrain_meaningHtmlIsNull(params)
        [ docToTrain: randomUntrainedDoc ]
    }

    def edit(Doc docInstance) {
        respond docInstance
    }

    @Transactional
    def saveAndNext() {
        Doc docToTrain = Doc.get(params.id)

        if (docToTrain == null) {
            notFound()
            return
        }

        docToTrain.train_meaningHtml = params.train_meaningHtml
        docToTrain.validate()

        if (docToTrain.hasErrors()) {
            respond docToTrain.errors, view: 'trainNext'
            return
        }

        docToTrain.save flush:true

        redirect action: 'trainNext'
    }

    @Transactional
    def save() {
        Doc docToTrain = Doc.get(params.id)

        if (docToTrain == null) {
            notFound()
            return
        }

        docToTrain.train_meaningHtml = params.train_meaningHtml
        docToTrain.validate()

        if (docToTrain.hasErrors()) {
            respond docToTrain.errors, view: 'trainNext'
            return
        }

        docToTrain.save flush:true

        redirect action: 'list'
    }

    @Transactional
    def removeFromTrain() {
        Doc docInstance = Doc.get(params.id)

        if (docInstance == null) {
            notFound()
            return
        }

        docInstance.train_meaningHtml = null
        docInstance.save flush:true

        redirect action: 'list'
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'doc.label', default: 'Doc'), params.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
