package com.github.dipodoc.webui.admin.controller.data

import com.github.dipodoc.webui.admin.domain.data.Topic
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class TopicController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Topic.list(params), model: [ topicCount: Topic.count() ]
    }

    def show(Topic topic) {
        respond topic
    }

    def create() {
        respond new Topic(params)
    }

    @Transactional
    def save(Topic topic) {
        if (topic == null) {
            notFound()
            return
        }

        if (topic.hasErrors()) {
            respond topic.errors, view: 'create'
            return
        }

        topic.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [ message(code: 'topic.label', default: 'Topic'), topic.id ])
                redirect topic
            }
            '*' { respond topic, [status: CREATED] }
        }
    }

    def edit(Topic topic) {
        respond topic
    }

    @Transactional
    def update(Topic topic) {
        if (topic == null) {
            notFound()
            return
        }

        if (topic.hasErrors()) {
            respond topic.errors, view: 'edit'
            return
        }

        topic.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [ message(code: 'topic.label', default: 'Topic'), topic.id ])
                redirect topic
            }
            '*' { respond topic, [status: OK] }
        }
    }

    @Transactional
    def delete(Topic topic) {
        if (topic == null) {
            notFound()
            return
        }

        topic.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'Topic.label', default: 'Topic'), topic.id ])
                redirect action:'list', method:'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'topic.label', default: 'Topic'), params.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
