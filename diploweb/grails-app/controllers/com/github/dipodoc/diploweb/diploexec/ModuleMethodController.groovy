package com.github.dipodoc.diploweb.diploexec

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ModuleMethodController {

    static allowedMethods = [ save: "POST", update: "PUT", delete: "DELETE" ]

    def create() {
        ModuleMethod moduleMethod = new ModuleMethod(params)
        moduleMethod.module = Module.get(params.moduleId)
        respond moduleMethod
    }

    @Transactional
    def save(ModuleMethod moduleMethodInstance) {
        if (moduleMethodInstance == null) {
            notFound()
            return
        }

        if (moduleMethodInstance.hasErrors()) {
            respond moduleMethodInstance.errors, view:'create'
            return
        }

        moduleMethodInstance.save flush:true

        redirect controller: 'module', action: 'show', id: moduleMethodInstance.module.id
    }

    def edit(ModuleMethod moduleMethodInstance) {
        respond moduleMethodInstance
    }

    @Transactional
    def update(ModuleMethod moduleMethodInstance) {
        if (moduleMethodInstance == null) {
            notFound()
            return
        }

        if (moduleMethodInstance.hasErrors()) {
            respond moduleMethodInstance.errors, view:'edit'
            return
        }

        moduleMethodInstance.save flush:true

        redirect controller: 'module', action: 'show', id: moduleMethodInstance.module.id
    }

    @Transactional
    def delete(ModuleMethod moduleMethodInstance) {
        if (moduleMethodInstance == null) {
            notFound()
            return
        }

        moduleMethodInstance.delete flush:true

        redirect controller: 'module', action: 'show', id: moduleMethodInstance.module.id
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'moduleMethod.label', default: 'ModuleMethod'), params.id])
                redirect action: "list", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
