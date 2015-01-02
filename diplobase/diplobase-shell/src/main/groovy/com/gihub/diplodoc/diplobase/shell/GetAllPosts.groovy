package com.gihub.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import org.springframework.context.support.GenericGroovyApplicationContext
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

/**
 * @author yaroslav.yermilov
 */
def COUNT = 10


def context = new GenericGroovyApplicationContext('diplobase.context')

def posts = context.getBean(PostRepository).findAll(new PageRequest(1, COUNT, new Sort(Sort.Direction.DESC, 'id')))

println "id".padRight(10) + "url".padRight(100) + "title".padRight(60)
posts.each {
    println "${it.id}".padRight(10) + "${it.url}".padRight(100) + new String(it.title.getBytes('cp866')).padRight(60)
}