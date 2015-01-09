package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.client.ProcessDataClient
import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diploexec.client.DiploexecClient
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class ProcessCommands implements CommandMarker {

    @Autowired
    ResourceLoader resourceLoader

    ProcessDataClient processDataClient = new ProcessDataClient('http://localhost:8080')
    DiploexecClient diploexecClient = new DiploexecClient('http://localhost:8080')

    JsonSlurper jsonSlurper = new JsonSlurper()

    @CliCommand(value = 'process list', help = 'list all processes')
    String list() {
        processDataClient.processes().collect(ProcessCommands.&shortToString).join('\n')
    }

    @CliCommand(value = 'process run', help = 'run process')
    String run(@CliOption(key = 'name', mandatory = true, help = 'process name') final String name,
               @CliOption(key = 'parameters', mandatory = true, help = 'path to paramters file') final String pathToParametersFile) {
        Process process = processDataClient.findOneByName(name)
        Map<String, Object> parameters = jsonSlurper.parse(resourceLoader.getResource("file:${pathToParametersFile}").file)

        ProcessRun processRun = diploexecClient.run(process, parameters)
        longToString(processRun)
    }

    @CliCommand(value = 'process get', help = 'get full description of process')
    String get(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        Process process = processDataClient.findOneByName(name)
        longToString(process)
    }

    @CliCommand(value = 'process remove', help = 'remove process')
    String remove(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        Process process = processDataClient.findOneByName(name)
        processDataClient.delete(process)
        'Done'
    }

    @CliCommand(value = 'process update', help = 'update process description')
    String update(@CliOption(key = 'name', mandatory = true, help = 'process name') final String name,
                  @CliOption(key = 'definition', mandatory = true, help = 'path to definition file') final String pathToDefinitionFile) {
        Process process = processDataClient.findOneByName(name)
        process.definition = resourceLoader.getResource("file:${pathToDefinitionFile}").file.text
        processDataClient.update(process)
        longToString(process)
    }

    private static shortToString(Process process) {
        "${process.id}".padLeft(5) + "${process.name}".padLeft(30) + "${process.lastUpdate}".padLeft(50)
    }

    private static longToString(Process process) {
        "id:".padRight(20) + "${process.id}\n" +
        "name:".padRight(20) + "${process.name}\n" +
        "last update:".padRight(20) + "${process.lastUpdate}\n" +
        "definition:\n" + "${process.definition}"
    }
}
