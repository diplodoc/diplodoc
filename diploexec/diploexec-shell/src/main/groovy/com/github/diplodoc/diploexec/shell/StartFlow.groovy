package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.domain.diplodata.Source
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */

def NAME = 'find-new-posts-from-source'
def INPUT = [ source: new Source(name: 'football.ua') ]


RestTemplate client = new RestTemplate()
client.postForLocation('http://localhost:8080/diploexec/api/flow/{name}/start', INPUT, [name: NAME])

println "Flow ${NAME} started..."