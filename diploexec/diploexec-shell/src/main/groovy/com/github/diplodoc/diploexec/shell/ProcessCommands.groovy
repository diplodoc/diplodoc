package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.client.diploexec.ProcessDataClient
import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diploexec.client.DiploexecClient
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Component
class ProcessCommands implements CommandMarker {

    @Autowired
    ResourceLoader resourceLoader

    @Autowired
    ProcessDataClient processDataClient

    DiploexecClient diploexecClient = new DiploexecClient('http://localhost:8080')

    JsonSlurper jsonSlurper = new JsonSlurper()

    @CliCommand(value = 'process list', help = 'list all processes')
    String list() {
        processDataClient.findAll().collect(ProcessCommands.&toSingleLineDescription).join('\n')
    }

    @CliCommand(value = 'process run', help = 'run process')
    String run(@CliOption(key = '', mandatory = true, help = 'process name') final String name,
               @CliOption(key = 'parameters', mandatory = false, help = 'path to paramters file') final String pathToParametersFile) {
        Process process = processDataClient.findOneByName(name)

        Map<String, Object> parameters = [:]
        if (pathToParametersFile) {
            Map<String, Object> jsonParameters = jsonSlurper.parse(resourceLoader.getResource("file:${pathToParametersFile}").file)
            parameters = jsonParameters.collectEntries { String key, Map<String, Object> parameter ->
                String type = parameter['type']
                parameter.remove('type')
                [ key,  Class.forName(type).newInstance(parameter) ]
            }
        }

        diploexecClient.run(process, parameters)
        'Started'
    }

    @CliCommand(value = 'process get', help = 'get full description of process')
    String get(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        toDescription(processDataClient.findOneByName(name))
    }

    @CliCommand(value = 'process disable', help = 'disable process')
    String disable(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        Process process = processDataClient.findOneByName(name)
        process.active = false
        processDataClient.save(process)
        'Disabled'
    }

    @CliCommand(value = 'process enable', help = 'enable process')
    String enable(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        Process process = processDataClient.findOneByName(name)
        process.active = true
        processDataClient.save(process)
        'Enabled'
    }

    @CliCommand(value = 'process update', help = 'update process description')
    String update(@CliOption(key = 'name', mandatory = true, help = 'process name') final String name,
                  @CliOption(key = 'definition', mandatory = true, help = 'path to definition file') final String pathToDefinitionFile) {
        Process process = processDataClient.findOneByName(name)
        process.definition = resourceLoader.getResource("file:${pathToDefinitionFile}").file.text
        process.lastUpdate = LocalDateTime.now()
        process = processDataClient.save(process)
        toDescription(process)
    }

    @CliCommand(value = 'process add', help = 'add new process')
    String add(@CliOption(key = 'name', mandatory = true, help = 'process name') final String name,
               @CliOption(key = 'definition', mandatory = true, help = 'path to definition file') final String pathToDefinitionFile) {
        Process process = new Process()
        process.name = name
        process.active = false
        process.lastUpdate = LocalDateTime.now()
        process.definition = resourceLoader.getResource("file:${pathToDefinitionFile}").file.text

        process = processDataClient.save(process)
        toDescription(process)
    }

    private static toSingleLineDescription(Process process) {
        "${process.id}".padRight(5) + "${process.name}".padLeft(40) + "${process.lastUpdate}".padLeft(30) + "${process.active?'active':'disabled'}".padLeft(10)
    }

    private static toDescription(Process process) {
        'id:'.padRight(20) + "${process.id}\n" +
        'name:'.padRight(20) + "${process.name}\n" +
        'last update:'.padRight(20) + "${process.lastUpdate}\n" +
        'status:'.padRight(20) + "${process.active?'active':'disabled'}\n" +
        'definition:\n' + "${process.definition}"
    }
}
