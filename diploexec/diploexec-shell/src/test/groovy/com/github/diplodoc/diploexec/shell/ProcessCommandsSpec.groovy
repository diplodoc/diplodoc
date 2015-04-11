package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRepository
import com.github.diplodoc.diploexec.client.DiploexecClient
import org.springframework.core.io.FileSystemResourceLoader
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCommandsSpec extends Specification {

    ProcessRepository processRepository = Mock(ProcessRepository)

    ProcessCommands processCommands = new ProcessCommands(processRepository: processRepository)

    def '`process list`'() {
        when:
            processRepository.findAll() >> [
                new Process(id: 1, name: 'process-1', lastUpdate: 'time-1', active: true),
                new Process(id: 2, name: 'process-2', lastUpdate: 'time-2', active: false)
            ]

        then:
            String actual = processCommands.list()

        expect:
            actual ==   '1                                   process-1                        time-1    active\n' +
                        '2                                   process-2                        time-2  disabled'
    }

    def '`process run`'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = '{"source": {"_type":"com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source","id":1,"newDocsFinderModule":"football.ua-new-docs-finder","name":"football.ua"}}'

            DiploexecClient diploexecClient = Mock(DiploexecClient)

        when:
            processCommands.diploexecClient = diploexecClient
            processCommands.resourceLoader = new FileSystemResourceLoader()
            processRepository.findOneByName('process') >> new Process(name: 'process')

            String actual = processCommands.run('process', tempFile.absolutePath)

        then:
            1 * diploexecClient.run(
                new Process(name: 'process'),
                [ 'source': new Source(id:1, name: 'football.ua', newDocsFinderModule: 'football.ua-new-docs-finder')]
            )

        expect:
            actual == 'Started'
    }

    def '`process get`'() {
        when:
            processRepository.findOneByName('process') >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time', active: true)

        then:
            String actual = processCommands.get('process')

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'status:             active\n' +
                        'definition:\n' +
                        'definition'
    }

    def '`process disable`'() {
        when:
            processRepository.findOneByName('process') >> new Process(name: 'process', active: true)

            String actual = processCommands.disable('process')

        then:
            1 * processRepository.save(new Process(name: 'process', active: false))

        expect:
            actual == 'Disabled'
    }

    def '`process enable`'() {
        when:
            processRepository.findOneByName('process') >> new Process(name: 'process', active: false)

            String actual = processCommands.enable('process')

        then:
            1 * processRepository.save(new Process(name: 'process', active: true))

        expect:
            actual == 'Enabled'
    }

    def '`process update` - definition changed'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = 'new-definition'

        when:
            processCommands.resourceLoader = new FileSystemResourceLoader()
            processRepository.findOneByName('process') >> new Process(id: 1, name: 'process', definition: 'old-definition', lastUpdate: 'old-times')

            String actual = processCommands.update('process', tempFile.absolutePath)

        then:
            1 * processRepository.save({ Process it ->
                it.name == 'process' && it.definition == 'new-definition' && it.lastUpdate != 'old-times'
            }) >> new Process(id: 1, name: 'process', definition: 'new-definition', lastUpdate: 'new-times', active: true)

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        new-times\n' +
                        'status:             active\n' +
                        'definition:\n' +
                        'new-definition'
    }

    def '`process update` - definition not changed'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = 'old-definition'

        when:
            processCommands.resourceLoader = new FileSystemResourceLoader()
            processRepository.findOneByName('process') >> new Process(id: 1, name: 'process', definition: 'old-definition', lastUpdate: 'old-times', active: true)

            String actual = processCommands.update('process', tempFile.absolutePath)

        then:
            0 * processRepository.save({ Process it ->
                it.name == 'process' && it.definition == 'new-definition' && it.lastUpdate != 'old-times'
            })

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        old-times\n' +
                        'status:             active\n' +
                        'definition:\n' +
                        'old-definition'
    }

    def '`process add`'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = 'definition'

        when:
            processCommands.resourceLoader = new FileSystemResourceLoader()

            String actual = processCommands.add('process', tempFile.absolutePath)

        then:
            1 * processRepository.save({ Process it ->
                it.name == 'process' && it.definition == 'definition' && it.lastUpdate != null
            }) >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time', active: false)

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'status:             disabled\n' +
                        'definition:\n' +
                        'definition'
    }

    def 'private static toSingleLineDescription(Process process) - active process'() {
        when:
            Process process = new Process(id: 1, name: 'process-1', lastUpdate: 'time-1', active: true)

        then:
            String actual = ProcessCommands.toSingleLineDescription(process)

        expect:
            actual ==   '1                                   process-1                        time-1    active'
    }

    def 'private static toSingleLineDescription(Process process) - disabled process'() {
        when:
            Process process = new Process(id: 1, name: 'process-1', lastUpdate: 'time-1', active: false)

        then:
            String actual = ProcessCommands.toSingleLineDescription(process)

        expect:
            actual ==   '1                                   process-1                        time-1  disabled'
    }

    def 'static toDescription(Process process) - active process'() {
        when:
            Process process =  new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time', active: true)

        then:
            String actual = ProcessCommands.toDescription(process)

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'status:             active\n' +
                        'definition:\n' +
                        'definition'
    }

    def 'static toDescription(Process process) - disabled process'() {
        when:
            Process process =  new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time', active: false)

        then:
            String actual = ProcessCommands.toDescription(process)

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'status:             disabled\n' +
                        'definition:\n' +
                        'definition'
    }
}
