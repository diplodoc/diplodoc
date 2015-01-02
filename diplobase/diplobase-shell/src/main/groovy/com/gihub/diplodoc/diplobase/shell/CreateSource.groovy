package com.gihub.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.diplodata.Source
import org.springframework.context.support.GenericGroovyApplicationContext

/**
 * @author yaroslav.yermilov
 */
def SOURCE = new Source(name: 'football.ua', newPostsFinderName: 'football.ua-new-posts-finder')


def context = new GenericGroovyApplicationContext('diplobase.context')

def result = context.getBean(SourceRepository).save(SOURCE)

println "id".padRight(10) + "name".padRight(100)
println "${result.id}".padRight(10) + "${result.name}".padRight(100)