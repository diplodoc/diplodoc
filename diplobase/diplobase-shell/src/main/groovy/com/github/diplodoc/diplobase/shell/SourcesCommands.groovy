package com.github.diplodoc.diplobase.shell

import groovy.json.JsonSlurper
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Component
class SourcesCommands implements CommandMarker {

    RestTemplate restTemplate = new RestTemplate()
    JsonSlurper jsonSlurper = new JsonSlurper()

    @CliCommand(value = 'sources list', help = 'list all sources')
    String list() {
        def sourcesJson = jsonSlurper.parseText(restTemplate.getForObject('http://localhost:8080/diplobase/sources', String))
        List sourcesLinks = (sourcesJson.links as List).findAll { link -> link.rel == 'source' }

        sourcesLinks.collect { sourceLink ->
            def sourceJson = jsonSlurper.parseText(restTemplate.getForObject(sourceLink.href, String))

            "${sourceLink.href.substring(sourceLink.href.lastIndexOf('/') + 1)}".padLeft(5) +
            "${sourceJson.name}".padLeft(30) +
            "${sourceJson.newPostsFinderModule}".padLeft(50)
        }.join('\n')
    }
}
