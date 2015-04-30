package com.github.dipodoc.webui.admin.controller.knu

import com.github.dipodoc.webui.admin.domain.data.Doc
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class KnuPostsController {

    static allowedMethods = [ delete: 'DELETE' ]

    def list() {
        params.max = 20
        params.sort = 'publishTime'
        params.order = 'desc'

        def knuPosts = Doc.where { knu == 'post' }

        respond knuPosts.list(params), model: [ docInstanceCount: knuPosts.count() ]
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
