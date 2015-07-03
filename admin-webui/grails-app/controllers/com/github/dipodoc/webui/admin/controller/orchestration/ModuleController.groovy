package com.github.dipodoc.webui.admin.controller.orchestration

import com.github.dipodoc.webui.admin.domain.orchestration.Module
import com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethod

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ModuleController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Module.list(params), model: [ moduleInstanceCount: Module.count() ]
    }

    def show(Module moduleInstance) {
        if (moduleInstance == null) {
            render status: NOT_FOUND
            return
        }

        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        def moduleMethodsList = ModuleMethod.where({ module == moduleInstance }).list()
        [ 'moduleInstance': moduleInstance, 'moduleMethodsList': moduleMethodsList ]
    }

    def create() {
        respond new Module(params)
    }

    def edit(Module moduleInstance) {
        if (moduleInstance == null) {
            render status: NOT_FOUND
            return
        }

        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        def moduleMethodsList = ModuleMethod.where({ module == moduleInstance }).list()
        [ 'moduleInstance': moduleInstance, 'moduleMethodsList': moduleMethodsList ]
    }

    @Transactional
    def save(Module moduleInstance) {
        if (moduleInstance == null) {
            notFound()
            return
        }

        if (moduleInstance.hasErrors()) {
            respond moduleInstance.errors, view:'create'
            return
        }

        moduleInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [ message(code: 'module.label', default: 'Module'), moduleInstance.id ])
                redirect moduleInstance
            }
            '*' { respond moduleInstance, [status: CREATED] }
        }
    }

    @Transactional
    def update(Module moduleInstance) {
        if (moduleInstance == null) {
            notFound()
            return
        }

        if (moduleInstance.hasErrors()) {
            respond moduleInstance.errors, view: 'edit'
            return
        }

        moduleInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [ message(code: 'Module.label', default: 'Module'), moduleInstance.id ])
                redirect moduleInstance
            }
            '*' { respond moduleInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Module moduleInstance) {
        if (moduleInstance == null) {
            notFound()
            return
        }

        moduleInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'Module.label', default: 'Module'), moduleInstance.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'module.label', default: 'Module'), params.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
