package com.github.dipodoc.webui.admin.controller.orchestration

import com.github.dipodoc.webui.admin.domain.orchestration.Module
import com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethod

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ModuleMethodController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def create() {
        // FIXIT: DIPLODOC-161. Extract all grails controllers logic to services
        ModuleMethod moduleMethod = new ModuleMethod(params)
        moduleMethod.module = Module.get(params.moduleId)
        respond moduleMethod
    }

    @Transactional
    def save(ModuleMethod moduleMethod) {
        if (moduleMethod == null) {
            notFound()
            return
        }

        if (moduleMethod.hasErrors()) {
            respond moduleMethod.errors, view:'create'
            return
        }

        moduleMethod.save flush:true

        redirect controller: 'module', action: 'show', id: moduleMethod.module.id
    }

    def edit(ModuleMethod moduleMethod) {
        respond moduleMethod
    }

    @Transactional
    def update(ModuleMethod moduleMethod) {
        if (moduleMethod == null) {
            notFound()
            return
        }

        if (moduleMethod.hasErrors()) {
            respond moduleMethod.errors, view:'edit'
            return
        }

        moduleMethod.save flush:true

        redirect controller: 'module', action: 'show', id: moduleMethod.module.id
    }

    @Transactional
    def delete(ModuleMethod moduleMethod) {
        if (moduleMethod == null) {
            notFound()
            return
        }

        moduleMethod.delete flush:true

        redirect controller: 'module', action: 'show', id: moduleMethod.module.id
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'moduleMethod.label', default: 'ModuleMethod'), params.id ])
                redirect controller: 'module', action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
