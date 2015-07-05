package com.github.dipodoc.webui.admin.controller.orchestration

import com.github.dipodoc.webui.admin.domain.orchestration.Process
import org.springframework.web.client.RestTemplate

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProcessController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Process.list(params), model: [ processCount: Process.count() ]
    }

    def show(Process process) {
        respond process
    }

    def create() {
        respond new Process(params)
    }

    def run(Process process) {
        respond process
    }

    def start(Process process) {
        // FIXIT: Extract all grails controllers logic to services
        def client = new RestTemplate()
        String url = "${System.getProperty 'orchestration_host'}/orchestration/process/${process.id}/run"

        def response = client.postForObject(url, null, String)
        String processRunId = response

        redirect controller: 'processRun', action: 'show', id: processRunId
    }

    @Transactional
    def save(Process process) {
        if (process == null) {
            notFound()
            return
        }

        if (process.hasErrors()) {
            respond process.errors, view: 'create'
            return
        }

        process.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [ message(code: 'process.label', default: 'Process'), process.id ])
                redirect process
            }
            '*' { respond process, [status: CREATED] }
        }
    }

    def edit(Process process) {
        respond process
    }

    @Transactional
    def update(Process process) {
        if (process == null) {
            notFound()
            return
        }

        if (process.hasErrors()) {
            respond process.errors, view: 'edit'
            return
        }

        process.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [ message(code: 'Process.label', default: 'Process'), process.id ])
                redirect process
            }
            '*' { respond process, [status: OK] }
        }
    }

    @Transactional
    def delete(Process process) {
        if (process == null) {
            notFound()
            return
        }

        process.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [ message(code: 'Process.label', default: 'Process'), process.id ])
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
