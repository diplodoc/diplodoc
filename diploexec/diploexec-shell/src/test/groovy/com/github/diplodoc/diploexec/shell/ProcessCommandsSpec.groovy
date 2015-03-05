package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.client.diploexec.ProcessDataClient
import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diploexec.client.DiploexecClient
import org.springframework.core.io.FileSystemResourceLoader
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCommandsSpec extends Specification {

    ProcessDataClient processDataClient = Mock(ProcessDataClient)
    ProcessCommands processCommands = new ProcessCommands(processDataClient: processDataClient)

    def 'process list'() {
        when:
            processDataClient.all() >> [
                new Process(id: 1, name: 'process-1', lastUpdate: 'time-1', active: true),
                new Process(id: 2, name: 'process-2', lastUpdate: 'time-2', active: false)
            ]

        then:
            String actual = processCommands.list()

        expect:
            actual ==   '1                                   process-1                        time-1    active\n' +
                        '2                                   process-2                        time-2  disabled'
    }

    def 'process run'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = '{"source": {"type":"com.github.diplodoc.diplobase.domain.mongodb.Source","id":1,"newPostsFinderModule":"football.ua-new-posts-finder","name":"football.ua"}}'

            DiploexecClient diploexecClient = Mock(DiploexecClient)

        when:
            processCommands.diploexecClient = diploexecClient
            processCommands.resourceLoader = new FileSystemResourceLoader()
            processDataClient.byName('process') >> new Process(name: 'process')

            String actual = processCommands.run('process', tempFile.absolutePath)

        then:
            1 * diploexecClient.run(
                new Process(name: 'process'),
                [ 'source': new Source(id:1, name: 'football.ua', newPostsFinderModule: 'football.ua-new-posts-finder')]
            )

        expect:
            actual == 'Started'
    }

    def '`process get` for active process'() {
        when:
            processDataClient.byName('process') >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time', active: true)

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

    def '`process get` for passive process'() {
        when:
            processDataClient.byName('process') >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time', active: false)

        then:
            String actual = processCommands.get('process')

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'status:             disabled\n' +
                        'definition:\n' +
                        'definition'
    }

    def 'process disable'() {
        when:
            processDataClient.byName('process') >> new Process(name: 'process', active: true)

            String actual = processCommands.disable('process')

        then:
            1 * processDataClient.save(new Process(name: 'process', active: false))

        expect:
            actual == 'Disabled'
    }

    def 'process enable'() {
        when:
            processDataClient.byName('process') >> new Process(name: 'process', active: false)

            String actual = processCommands.enable('process')

        then:
            1 * processDataClient.save(new Process(name: 'process', active: true))

        expect:
            actual == 'Enabled'
    }

    def 'process update'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = 'definition'

        when:
            processCommands.resourceLoader = new FileSystemResourceLoader()
            processDataClient.byName('process') >> new Process(id: 1, name: 'process')

            String actual = processCommands.update('process', tempFile.absolutePath)

        then:
            1 * processDataClient.save({ Process it ->
                it.name == 'process' && it.definition == 'definition' && it.lastUpdate != null
            }) >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time', active: true)

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'status:             active\n' +
                        'definition:\n' +
                        'definition'
    }

    def 'process add'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = 'definition'

        when:
            processCommands.resourceLoader = new FileSystemResourceLoader()

            String actual = processCommands.add('process', tempFile.absolutePath)

        then:
            1 * processDataClient.save({ Process it ->
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
}
