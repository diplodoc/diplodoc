package com.github.dipodoc.webui.admin.controller.data

import com.github.dipodoc.webui.admin.domain.data.Doc

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class DocController {

    static allowedMethods = [ delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Doc.list(params), model: [ docCount: Doc.count() ]
    }

    def show(Doc doc) {
        respond doc
    }

    @Transactional
    def delete(Doc doc) {
        if (doc == null) {
            notFound()
            return
        }

        doc.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'doc.label', default: 'Doc'), doc.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
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
