package com.github.dipodoc.diploweb.diploexec

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ModuleMethodRunController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ModuleMethodRun.list(params), model:[moduleMethodRunInstanceCount: ModuleMethodRun.count()]
    }

    def show(ModuleMethodRun moduleMethodRunInstance) {
        respond moduleMethodRunInstance
    }
}
