package com.github.diplodoc.diplobase.shell

import org.springframework.beans.factory.annotation.Autowired
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

    @CliCommand(value = 'sources list', help = 'list all sources')
    String list() {
        List sources = restTemplate.getForObject('http://localhost:8080/diplobase/sources', List.class)
        sources.each {
            println it
        }
    }
}
