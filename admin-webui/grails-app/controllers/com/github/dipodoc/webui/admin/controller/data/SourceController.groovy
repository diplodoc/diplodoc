package com.github.dipodoc.webui.admin.controller.data

import com.github.dipodoc.webui.admin.domain.data.Source

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SourceController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Source.list(params), model: [ sourceInstanceCount: Source.count() ]
    }

    def show(Source sourceInstance) {
        respond sourceInstance
    }

    def create() {
        respond new Source(params)
    }

    @Transactional
    def save(Source sourceInstance) {
        if (sourceInstance == null) {
            notFound()
            return
        }

        if (sourceInstance.hasErrors()) {
            respond sourceInstance.errors, view: 'create'
            return
        }

        sourceInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [ message(code: 'source.label', default: 'Source'), sourceInstance.id ])
                redirect sourceInstance
            }
            '*' { respond sourceInstance, [ status: CREATED ] }
        }
    }

    def edit(Source sourceInstance) {
        respond sourceInstance
    }

    @Transactional
    def update(Source sourceInstance) {
        if (sourceInstance == null) {
            notFound()
            return
        }

        if (sourceInstance.hasErrors()) {
            respond sourceInstance.errors, view: 'edit'
            return
        }

        sourceInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [ message(code: 'Source.label', default: 'Source'), sourceInstance.id ])
                redirect sourceInstance
            }
            '*' { respond sourceInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Source sourceInstance) {
        if (sourceInstance == null) {
            notFound()
            return
        }

        sourceInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'Source.label', default: 'Source'), sourceInstance.id ])
                redirect action:'list', method:'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'source.label', default: 'Source'), params.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
