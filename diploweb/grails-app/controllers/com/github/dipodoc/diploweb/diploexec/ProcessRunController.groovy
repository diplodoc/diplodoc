package com.github.dipodoc.diploweb.diploexec

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProcessRunController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ProcessRun.list(params), model:[processRunInstanceCount: ProcessRun.count()]
    }

    def show(ProcessRun processRunInstance) {
        respond processRunInstance
    }

    def create() {
        respond new ProcessRun(params)
    }

    @Transactional
    def save(ProcessRun processRunInstance) {
        if (processRunInstance == null) {
            notFound()
            return
        }

        if (processRunInstance.hasErrors()) {
            respond processRunInstance.errors, view:'create'
            return
        }

        processRunInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'processRun.label', default: 'ProcessRun'), processRunInstance.id])
                redirect processRunInstance
            }
            '*' { respond processRunInstance, [status: CREATED] }
        }
    }

    def edit(ProcessRun processRunInstance) {
        respond processRunInstance
    }

    @Transactional
    def update(ProcessRun processRunInstance) {
        if (processRunInstance == null) {
            notFound()
            return
        }

        if (processRunInstance.hasErrors()) {
            respond processRunInstance.errors, view:'edit'
            return
        }

        processRunInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ProcessRun.label', default: 'ProcessRun'), processRunInstance.id])
                redirect processRunInstance
            }
            '*'{ respond processRunInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(ProcessRun processRunInstance) {

        if (processRunInstance == null) {
            notFound()
            return
        }

        processRunInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ProcessRun.label', default: 'ProcessRun'), processRunInstance.id])
                redirect action:"list", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'processRun.label', default: 'ProcessRun'), params.id])
                redirect action: "list", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
