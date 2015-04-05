package com.github.dipodoc.diploweb.diplodata

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PostController {

    static allowedMethods = [ delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Post.list(params), model: [ postInstanceCount: Post.count() ]
    }

    def show(Post postInstance) {
        respond postInstance
    }

    @Transactional
    def delete(Post postInstance) {
        if (postInstance == null) {
            notFound()
            return
        }

        postInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'post.label', default: 'Post'), postInstance.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'post.label', default: 'Post'), params.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
