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
