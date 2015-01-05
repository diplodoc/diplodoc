package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.domain.diplodata.Source
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Component
class FlowCommands implements CommandMarker {

    RestTemplate restTemplate = new RestTemplate()

    @CliCommand(value = 'flow start', help = 'start new flow')
    String start() {
        def NAME = 'find-new-posts-from-source'
        def INPUT = [ source: new Source(name: 'football.ua') ]

        restTemplate.postForLocation('http://localhost:8080/diploexec/api/flow/{name}/start', INPUT, [name: NAME])

        return "Flow ${NAME} started..."
    }
}
