package com.github.dipodoc.diploweb.diploexec



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProcessRunParameterController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ProcessRunParameter.list(params), model:[processRunParameterInstanceCount: ProcessRunParameter.count()]
    }

    def show(ProcessRunParameter processRunParameterInstance) {
        respond processRunParameterInstance
    }

    def create() {
        respond new ProcessRunParameter(params)
    }

    @Transactional
    def save(ProcessRunParameter processRunParameterInstance) {
        if (processRunParameterInstance == null) {
            notFound()
            return
        }

        if (processRunParameterInstance.hasErrors()) {
            respond processRunParameterInstance.errors, view:'create'
            return
        }

        processRunParameterInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'processRunParameter.label', default: 'ProcessRunParameter'), processRunParameterInstance.id])
                redirect processRunParameterInstance
            }
            '*' { respond processRunParameterInstance, [status: CREATED] }
        }
    }

    def edit(ProcessRunParameter processRunParameterInstance) {
        respond processRunParameterInstance
    }

    @Transactional
    def update(ProcessRunParameter processRunParameterInstance) {
        if (processRunParameterInstance == null) {
            notFound()
            return
        }

        if (processRunParameterInstance.hasErrors()) {
            respond processRunParameterInstance.errors, view:'edit'
            return
        }

        processRunParameterInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ProcessRunParameter.label', default: 'ProcessRunParameter'), processRunParameterInstance.id])
                redirect processRunParameterInstance
            }
            '*'{ respond processRunParameterInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(ProcessRunParameter processRunParameterInstance) {

        if (processRunParameterInstance == null) {
            notFound()
            return
        }

        processRunParameterInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ProcessRunParameter.label', default: 'ProcessRunParameter'), processRunParameterInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'processRunParameter.label', default: 'ProcessRunParameter'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
