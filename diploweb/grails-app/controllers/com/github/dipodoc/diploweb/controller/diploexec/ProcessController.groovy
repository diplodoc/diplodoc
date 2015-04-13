package com.github.dipodoc.diploweb.controller.diploexec

import com.github.dipodoc.diploweb.domain.diploexec.Process
import grails.plugins.rest.client.RestBuilder
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class ProcessController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Process.list(params), model: [ processInstanceCount: Process.count() ]
    }

    def show(Process processInstance) {
        respond processInstance
    }

    def create() {
        respond new Process(params)
    }

    def run(Process processInstance) {
        respond processInstance
    }

    def start(Process processInstance) {
        def client = new RestBuilder()
        def response = client.post("http://localhost:8080/diploexec/process/${processInstance.id}/run") {
            contentType 'text/plain'
        }
        String processRunId = "${response?.text}"

        redirect controller: 'processRun', action: 'show', id: processRunId
    }

    @Transactional
    def save(Process processInstance) {
        if (processInstance == null) {
            notFound()
            return
        }

        if (processInstance.hasErrors()) {
            respond processInstance.errors, view: 'create'
            return
        }

        processInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [ message(code: 'process.label', default: 'Process'), processInstance.id ])
                redirect processInstance
            }
            '*' { respond processInstance, [status: CREATED] }
        }
    }

    def edit(Process processInstance) {
        respond processInstance
    }

    @Transactional
    def update(Process processInstance) {
        if (processInstance == null) {
            notFound()
            return
        }

        if (processInstance.hasErrors()) {
            respond processInstance.errors, view: 'edit'
            return
        }

        processInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [ message(code: 'Process.label', default: 'Process'), processInstance.id ])
                redirect processInstance
            }
            '*' { respond processInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Process processInstance) {
        if (processInstance == null) {
            notFound()
            return
        }

        processInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'Process.label', default: 'Process'), processInstance.id ])
                redirect action: 'list', method:'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'process.label', default: 'Process'), params.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
