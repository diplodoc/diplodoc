package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface GroovyBindingEnhancer {

    Binding enhance(Binding binding, Process process, Map input, ProcessRun processRun)
}