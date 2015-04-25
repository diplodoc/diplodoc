package com.github.dipodoc.diploweb.controller.knu

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class KnuDocumentsController {

    static allowedMethods = [ delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def knuDocuments = Doc.where { knu == 'document' }

        respond knuDocuments.list(params), model: [ docInstanceCount: knuDocuments.count() ]
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
