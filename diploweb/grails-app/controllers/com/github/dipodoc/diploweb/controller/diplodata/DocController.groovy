package com.github.dipodoc.diploweb.controller.diplodata

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class DocController {

    static allowedMethods = [ delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Doc.list(params), model: [ docInstanceCount: Doc.count() ]
    }

    def show(Doc docInstance) {
        respond docInstance
    }

    @Transactional
    def delete(Doc docInstance) {
        if (docInstance == null) {
            notFound()
            return
        }

        docInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'doc.label', default: 'Doc'), docInstance.id ])
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
