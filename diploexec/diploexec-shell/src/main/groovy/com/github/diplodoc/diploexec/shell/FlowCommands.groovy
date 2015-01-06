package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.client.FlowClient
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.domain.diploexec.Flow
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

    FlowClient flowClient = new FlowClient('http://localhost:8080')

    @CliCommand(value = 'flow list', help = 'list all flows')
    String list() {
        flowClient.flows()
                        .collect { Flow flow ->
                            "${flow.id}".padLeft(5) + "${flow.name}".padLeft(30)
                        }
                        .join('\n')
    }

    @CliCommand(value = 'flow get', help = 'get full description of flow')
    String get(@CliOption(key = '', mandatory = true, help = 'flow name') final String name) {
        Flow flow = flowClient.findOneByName(name)

        "id:".padRight(20) + "${flow.id}\n" +
        "name:".padRight(20) + "${flow.name}\n" +
        "definition:\n" + "${flow.definition}"
    }
}
