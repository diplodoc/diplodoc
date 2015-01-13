package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.diplodata.Source
import groovy.json.JsonOutput
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

    @CliCommand(value = 'sources dump', help = 'dump source to file')
    String dump(@CliOption(key = 'name', mandatory = true, help = 'source name') final String name,
               @CliOption(key = 'path', mandatory = true, help = 'path to dump file') final String path) {
        def sourcesJson = jsonSlurper.parseText(restTemplate.getForObject('http://localhost:8080/diplobase/sources', String))
        List sourcesLinks = (sourcesJson.links as List).findAll { link -> link.rel == 'source' }

        Source source = sourcesLinks
                            .collect { sourceLink ->
                                def sourceJson = jsonSlurper.parseText(restTemplate.getForObject(sourceLink.href, String))
                                new Source(
                                    id: Long.parseLong("${sourceLink.href.substring(sourceLink.href.lastIndexOf('/') + 1)}"),
                                    name: "${sourceJson.name}",
                                    newPostsFinderModule: "${sourceJson.newPostsFinderModule}"
                                )
                            }
                            .find { Source source -> source.name == name }

        Map<String, String> sourceProperties = source.properties.collectEntries { String key, Object value ->
            key.toString() != 'class' ? [ key, value ] : [ 'type', Source.class.name ]
        }
        new File(path).text = JsonOutput.toJson(sourceProperties)
    }
}
