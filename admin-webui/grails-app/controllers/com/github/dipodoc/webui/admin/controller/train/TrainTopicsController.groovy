package com.github.dipodoc.webui.admin.controller.train

import com.github.dipodoc.webui.admin.domain.data.Doc
import com.github.dipodoc.webui.admin.domain.data.Topic
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
class TrainTopicsController {

    static allowedMethods = [ save: 'PUT', saveAndNext: 'PUT', addTopicToTrainingSet: 'PUT', removeFromTrain: 'DELETE', removeTopicFromTrainingSet: 'DELETE' ]

    Random random = new Random()

    def list(Integer max) {
        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        params.max = Math.min(max ?: 10, 100)
        def trainSet = Doc.where { train_topics != null && train_topics.size() > 0 }

        respond trainSet.list(params), model: [ docCount: trainSet.count() ]
    }

    def trainNext() {
        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
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

    def edit(Doc doc) {
        respond doc
    }

    @Transactional
    def removeFromTrain() {
        Doc doc = Doc.get(params.id)

        if (doc == null) {
            notFound()
            return
        }

        doc.train_topics = []
        doc.save flush:true

        redirect action: 'list'
    }

    @Transactional
    def removeTopicFromTrainingSet() {
        Doc doc = Doc.get(params.docId)
        Topic topic = Topic.get(params.topicId)

        if (doc == null || topic == null) {
            notFound()
            return
        }

        doc.train_topics.remove(topic)
        doc.save flush:true

        redirect action: params.redirectTo, id: doc.id, params: [ id: doc.id ]
    }

    @Transactional
    def addTopicToTrainingSet() {
        Doc doc = Doc.get(params.docId)
        Topic topic = Topic.get(params.topicId)

        if (doc == null || topic == null) {
            notFound()
            return
        }

        doc.train_topics.add(topic)
        doc.save flush:true

        redirect action: params.redirectTo, id: doc.id, params: [ id: doc.id ]
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
