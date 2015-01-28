package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRunParameter
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRepository
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
    ProcessRepository processRepository

    DiploexecClient diploexecClient = new DiploexecClient('http://localhost:8080')

    JsonSlurper jsonSlurper = new JsonSlurper()

    @CliCommand(value = 'process list', help = 'list all processes')
    String list() {
        processRepository.findAll().collect(ProcessCommands.&shortToString).join('\n')
    }

    @CliCommand(value = 'process run', help = 'run process')
    String run(@CliOption(key = 'name', mandatory = true, help = 'process name') final String name,
               @CliOption(key = 'parameters', mandatory = true, help = 'path to paramters file') final String pathToParametersFile) {
        Process process = processRepository.findOneByName(name)
        Map<String, Object> jsonParameters = jsonSlurper.parse(resourceLoader.getResource("file:${pathToParametersFile}").file)
        Map<String, Object> parameters = jsonParameters.collectEntries { String key, Map<String, Object> parameter ->
            String type = parameter['type']
            parameter.remove('type')
            [ key,  Class.forName(type).newInstance(parameter) ]
        }

        diploexecClient.run(process, parameters)
        'Started'
    }

    @CliCommand(value = 'process get', help = 'get full description of process')
    String get(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        Process process = processRepository.findOneByName(name)
        longToString(process)
    }

    @CliCommand(value = 'process remove', help = 'remove process')
    String remove(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        Process process = processRepository.findOneByName(name)
        processRepository.delete(process)
        'Done'
    }

    @CliCommand(value = 'process update', help = 'update process description')
    String update(@CliOption(key = 'name', mandatory = true, help = 'process name') final String name,
                  @CliOption(key = 'definition', mandatory = true, help = 'path to definition file') final String pathToDefinitionFile) {
        Process process = processRepository.findOneByName(name)
        process.definition = resourceLoader.getResource("file:${pathToDefinitionFile}").file.text
        process.lastUpdate = LocalDateTime.now().toString()
        processRepository.save(process)
        longToString(process)
    }

    @CliCommand(value = 'process add', help = 'add new process')
    String add(@CliOption(key = 'name', mandatory = true, help = 'process name') final String name,
               @CliOption(key = 'definition', mandatory = true, help = 'path to definition file') final String pathToDefinitionFile) {
        Process process = new Process()
        process.name = name
        process.lastUpdate = LocalDateTime.now().toString()
        process.definition = resourceLoader.getResource("file:${pathToDefinitionFile}").file.text

        processRepository.save(process)
        longToString(process)
    }

    private static shortToString(Process process) {
        "${process.id}".padRight(5) + "${process.name}".padLeft(30) + "${process.lastUpdate}".padLeft(50)
    }

    private static longToString(Process process) {
        'id:'.padRight(20) + "${process.id}\n" +
        'name:'.padRight(20) + "${process.name}\n" +
        'last update:'.padRight(20) + "${process.lastUpdate}\n" +
        'definition:\n' + "${process.definition}"
    }
}
