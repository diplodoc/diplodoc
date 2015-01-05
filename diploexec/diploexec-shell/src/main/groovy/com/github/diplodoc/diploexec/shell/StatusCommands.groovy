package com.github.diplodoc.diploexec.shell

import groovy.json.JsonSlurper
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Component
class StatusCommands implements CommandMarker {

    RestTemplate restTemplate = new RestTemplate()

    @CliCommand(value = 'status', help = 'current diploexec runtime status')
    String status() {
        def status = restTemplate.getForObject('http://localhost:8080/diploexec/api/status', List.class)

        String result = ''
        result += 'RUNNING\n'
        result += 'id'.padRight(40) + 'name'.padRight(30) + 'status'.padRight(10) + 'start'.padRight(30) + 'end'.padRight(30) + 'description'.padRight(80) + '\n'
        status.findAll { it.status == 'RUNNING' }.each {
            result += "${it.id}".padRight(40) + "${it.name}".padRight(30) + "${it.status}".padRight(10) + "${it.startTime}".padRight(30) + "${it.endTime}".padRight(30) + "${it.description}".padRight(80) + '\n'
        }
        result += '\n'

        result += 'WAITING\n'
        result += 'id'.padRight(40) + 'name'.padRight(30) + 'status'.padRight(10) + 'start'.padRight(30) + 'end'.padRight(30) + 'description'.padRight(80) + '\n'
        status.findAll { it.status == 'WAITING' }.each {
            result += "${it.id}".padRight(40) + "${it.name}".padRight(30) + "${it.status}".padRight(10) + "${it.startTime}".padRight(30) + "${it.endTime}".padRight(30) + "${it.description}".padRight(80) + '\n'
        }
        result += '\n'

        result += 'FINISHED\n'
        result += 'id'.padRight(40) + 'name'.padRight(30) + 'status'.padRight(10) + 'start'.padRight(30) + 'end'.padRight(30) + 'description'.padRight(80) + '\n'
        status.findAll { it.status == 'FINISHED' }.each {
            result += "${it.id}".padRight(40) + "${it.name}".padRight(30) + "${it.status}".padRight(10) + "${it.startTime}".padRight(30) + "${it.endTime}".padRight(30) + "${it.description}".padRight(80) + '\n'
        }
        result += '\n'

        return result
    }
}
