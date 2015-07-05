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
        respond Module.list(params), model: [ moduleCount: Module.count() ]
    }

    def show(Module module) {
        if (module == null) {
            render status: NOT_FOUND
            return
        }

        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        def moduleMethodsList = ModuleMethod.where({ module == module }).list()
        [ 'module': module, 'moduleMethodsList': moduleMethodsList ]
    }

    def create() {
        respond new Module(params)
    }

    def edit(Module module) {
        if (module == null) {
            render status: NOT_FOUND
            return
        }

        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        def moduleMethodsList = ModuleMethod.where({ module == module }).list()
        [ 'module': module, 'moduleMethodsList': moduleMethodsList ]
    }

    @Transactional
    def save(Module module) {
        if (module == null) {
            notFound()
            return
        }

        if (module.hasErrors()) {
            respond module.errors, view:'create'
            return
        }

        module.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [ message(code: 'module.label', default: 'Module'), module.id ])
                redirect module
            }
            '*' { respond module, [status: CREATED] }
        }
    }

    @Transactional
    def update(Module module) {
        if (module == null) {
            notFound()
            return
        }

        if (module.hasErrors()) {
            respond module.errors, view: 'edit'
            return
        }

        module.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [ message(code: 'Module.label', default: 'Module'), module.id ])
                redirect module
            }
            '*' { respond module, [status: OK] }
        }
    }

    @Transactional
    def delete(Module module) {
        if (module == null) {
            notFound()
            return
        }

        module.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'Module.label', default: 'Module'), module.id ])
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
