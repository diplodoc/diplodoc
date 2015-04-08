package com.github.dipodoc.diploweb.controller.diploexec

import com.github.dipodoc.diploweb.domain.diploexec.ProcessRun
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProcessRunController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ProcessRun.list(params), model: [ processRunInstanceCount: ProcessRun.count() ]
    }

    def show(ProcessRun processRunInstance) {
        respond processRunInstance
    }
}
