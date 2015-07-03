package com.github.dipodoc.webui.admin.controller.orchestration

import com.github.dipodoc.webui.admin.domain.orchestration.ProcessRun
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProcessRunController {

    static allowedMethods = [ save: 'POST', update: 'PUT', delete: 'DELETE' ]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = 'startTime'
        params.order = 'desc'
        respond ProcessRun.list(params), model: [ processRunInstanceCount: ProcessRun.count() ]
    }

    def show(ProcessRun processRunInstance) {
        respond processRunInstance
    }
}
