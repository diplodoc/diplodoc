package com.gihub.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
import org.springframework.context.support.GenericGroovyApplicationContext

/**
 * @author yaroslav.yermilov
 */

def context = new GenericGroovyApplicationContext('diplobase.context')

def posts = context.getBean(SourceRepository).findAll()

println "id".padRight(10) + "name".padRight(100)
posts.each {
    println "${it.id}".padRight(10) + "${it.name}".padRight(100)
}