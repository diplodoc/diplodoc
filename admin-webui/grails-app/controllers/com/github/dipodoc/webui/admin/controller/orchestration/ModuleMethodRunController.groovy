package com.github.dipodoc.webui.admin.controller.orchestration

import com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethodRun
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class ModuleMethodRunController {

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = 'startTime'
        params.order = 'desc'
        respond ModuleMethodRun.list(params), model: [ moduleMethodRunInstanceCount: ModuleMethodRun.count() ]
    }

    def show(ModuleMethodRun moduleMethodRunInstance) {
        respond moduleMethodRunInstance
    }
}
