package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.domain.diplodata.Source
import groovy.json.JsonSlurper
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Component
class FlowCommands implements CommandMarker {

    RestTemplate restTemplate = new RestTemplate()
    JsonSlurper jsonSlurper = new JsonSlurper()

    @CliCommand(value = 'flow list', help = 'list all flows')
    String list() {
        def flowsJson = jsonSlurper.parseText(restTemplate.getForObject('http://localhost:8080/diplobase/flows', String))
        List flowsLinks = (flowsJson.links as List).findAll { link -> link.rel == 'flow' }

        flowsLinks.collect { flowLink ->
            def flowJson = jsonSlurper.parseText(restTemplate.getForObject(flowLink.href, String))

            "${flowJson.'_links'.self.href.substring(flowJson.'_links'.self.href.lastIndexOf('/') + 1)}".padLeft(5) +
            "${flowJson.name}".padLeft(30)
        }.join('\n')
    }

    @CliCommand(value = 'flow get', help = 'get full description of flow')
    String get(@CliOption(key = [ '' ], mandatory = true, help = 'flow name') final String name) {
        def flowsJson = jsonSlurper.parseText(restTemplate.getForObject("http://localhost:8080/diplobase/flows/search/findOneByName?name=${name}", String))
        List flowsLinks = (flowsJson.links as List).findAll { link -> link.rel == 'flow' }

        flowsLinks.collect { flowLink ->
            def flowJson = jsonSlurper.parseText(restTemplate.getForObject(flowLink.href, String))

            "${flowJson.'_links'.self.href.substring(flowJson.'_links'.self.href.lastIndexOf('/') + 1)}".padLeft(5) +
                    "${flowJson.name}".padLeft(30)
        }.join('\n')
    }

    @CliCommand(value = 'flow start', help = 'start new flow')
    String start() {
        def NAME = 'find-new-posts-from-source'
        def INPUT = [ source: new Source(name: 'football.ua') ]

        restTemplate.postForLocation('http://localhost:8080/diploexec/api/v1/flow/{name}/start', INPUT, [name: NAME])

        return "Flow ${NAME} started..."
    }
}
