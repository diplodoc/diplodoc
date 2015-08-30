package com.github.diplodoc.orchestration

/**
 * @author yaroslav.yermilov
 */
interface GroovyBindingEnhancer {

    Binding enhance(Binding binding, Map context)
}