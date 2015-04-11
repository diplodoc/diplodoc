package com.github.dipodoc.diploweb.controller.train

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import com.github.dipodoc.diploweb.domain.diplodata.Topic
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class TrainTopicsController {

    static allowedMethods = [ save: 'PUT', saveAndNext: 'PUT', addTopicToTrainingSet: 'PUT', removeFromTrain: 'DELETE', removeTopicFromTrainingSet: 'DELETE' ]

    Random random = new Random()

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def trainSet = Doc.where { train_topics != null && train_topics.size() > 0 }

        respond trainSet.list(params), model: [ docInstanceCount: trainSet.count() ]
    }

    def trainNext() {
        if (params.id == null) {
            int untrainedCount = Doc.where({ train_topics == null || train_topics.isEmpty() }).count()
            int index = random.nextInt(untrainedCount)
            def params = [ offset: index, max: 1 ]

            Doc randomUntrainedDoc = Doc.where({ train_topics == null || train_topics.isEmpty() }).list(params)[0]
            [ docToTrain: randomUntrainedDoc ]
        } else {
            [ docToTrain: Doc.get(params.id) ]
        }
    }

    def edit(Doc docInstance) {
        respond docInstance
    }

    @Transactional
    def removeFromTrain() {
        Doc docInstance = Doc.get(params.id)

        if (docInstance == null) {
            notFound()
            return
        }

        docInstance.train_topics = []
        docInstance.save flush:true

        redirect action: 'list'
    }

    @Transactional
    def removeTopicFromTrainingSet() {
        Doc docInstance = Doc.get(params.docId)
        Topic topicInstance = Topic.get(params.topicId)

        if (docInstance == null || topicInstance == null) {
            notFound()
            return
        }

        docInstance.train_topics.remove(topicInstance)
        docInstance.save flush:true

        redirect action: params.redirectTo, id: docInstance.id, params: [ id: docInstance.id ]
    }

    @Transactional
    def addTopicToTrainingSet() {
        Doc docInstance = Doc.get(params.docId)
        Topic topicInstance = Topic.get(params.topicId)

        if (docInstance == null || topicInstance == null) {
            notFound()
            return
        }

        docInstance.train_topics.add(topicInstance)
        docInstance.save flush:true

        redirect action: params.redirectTo, id: docInstance.id, params: [ id: docInstance.id ]
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
