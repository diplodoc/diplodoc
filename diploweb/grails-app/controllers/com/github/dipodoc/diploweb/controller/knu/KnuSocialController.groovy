package com.github.dipodoc.diploweb.controller.knu

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class KnuSocialController {

    static allowedMethods = [ delete: 'DELETE' ]

    def list() {
        params.max = 20
        params.sort = 'publishTime'
        params.order = 'desc'

        def knuSocials = Doc.where { knu == 'social' }

        respond knuSocials.list(params), model: [ docInstanceCount: knuSocials.count() ]
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
