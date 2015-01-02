package com.gihub.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import org.springframework.context.support.GenericGroovyApplicationContext

/**
 * @author yaroslav.yermilov
 */
def URL = 'http://football.ua/ukraine/253533-karpaty-zarja-nakanune.html'


def context = new GenericGroovyApplicationContext('diplobase.context')

def post = context.getBean(PostRepository).findByUrl(URL).get(0)

println "id".padRight(20) + "${post.id}"
println "url".padRight(20) + "${post.url}"
println "source".padRight(20) + new String(post.source?.name?.getBytes('cp866')?:'<EMPTY>')
println "title".padRight(20) + new String(post.title?.getBytes('cp866')?:'<EMPTY>')
println "meaning text"
println ("=" * 20)
println new String(post.meaningText?.getBytes('cp866')?:'<EMPTY>')
println ("=" * 20)
println "html"
println ("=" * 20)
println new String(post.html?.getBytes('cp866')?:'<EMPTY>')
println ("=" * 20)