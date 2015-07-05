package com.github.dipodoc.webui.admin.controller.data

import com.github.dipodoc.webui.admin.domain.data.Source

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SourceController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Source.list(params), model: [ sourceCount: Source.count() ]
    }

    def show(Source source) {
        respond source
    }

    def create() {
        respond new Source(params)
    }

    @Transactional
    def save(Source source) {
        if (source == null) {
            notFound()
            return
        }

        if (source.hasErrors()) {
            respond source.errors, view: 'create'
            return
        }

        source.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [ message(code: 'source.label', default: 'Source'), source.id ])
                redirect source
            }
            '*' { respond source, [ status: CREATED ] }
        }
    }

    def edit(Source source) {
        respond source
    }

    @Transactional
    def update(Source source) {
        if (source == null) {
            notFound()
            return
        }

        if (source.hasErrors()) {
            respond source.errors, view: 'edit'
            return
        }

        source.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [ message(code: 'Source.label', default: 'Source'), source.id ])
                redirect source
            }
            '*' { respond source, [status: OK] }
        }
    }

    @Transactional
    def delete(Source source) {
        if (source == null) {
            notFound()
            return
        }

        source.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'Source.label', default: 'Source'), source.id ])
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
