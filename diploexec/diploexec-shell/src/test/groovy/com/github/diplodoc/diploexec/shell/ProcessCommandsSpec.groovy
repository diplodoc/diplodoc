package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.client.diploexec.ProcessDataClient
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diploexec.client.DiploexecClient
import org.springframework.core.io.FileSystemResourceLoader
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessCommandsSpec extends Specification {

    ProcessDataClient processDataClient = Mock(ProcessDataClient)
    ProcessCommands processCommands = new ProcessCommands(processDataClient: processDataClient)

    def '`process list` command'() {
        when:
            processDataClient.findAll() >> [
                new Process(id: 1, name: 'process-1', lastUpdate: 'time-1'),
                new Process(id: 2, name: 'process-2', lastUpdate: 'time-2')
            ]

        then:
            String actual = processCommands.list()

        expect:
            actual ==   '1                         process-1                                            time-1\n' +
                        '2                         process-2                                            time-2'
    }

    def '`process run` command'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = '{"source": {"type":"com.github.diplodoc.diplobase.domain.diplodata.Source","id":1,"newPostsFinderModule":"football.ua-new-posts-finder","name":"football.ua"}}'

            DiploexecClient diploexecClient = Mock(DiploexecClient)

        when:
            processCommands.diploexecClient = diploexecClient
            processCommands.resourceLoader = new FileSystemResourceLoader()
            processDataClient.findOneByName('process') >> new Process(name: 'process')

            String actual = processCommands.run('process', tempFile.absolutePath)

        then:
            1 * diploexecClient.run(
                new Process(name: 'process'),
                [ 'source': new Source(id:1, name: 'football.ua', newPostsFinderModule: 'football.ua-new-posts-finder')]
            )

        expect:
            actual == 'Started'
    }

    def '`process get` command'() {
        when:
            processDataClient.findOneByName('process') >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time')

        then:
            String actual = processCommands.get('process')

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'definition:\n' +
                        'definition'
    }

    def '`process remove` command'() {
        when:
            processDataClient.findOneByName('process') >> new Process(name: 'process')

            String actual = processCommands.remove('process')

        then:
            1 * processDataClient.delete(new Process(name: 'process'))

        expect:
            actual == 'Removed'
    }

    def '`process update` command'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = 'definition'

        when:
            processCommands.resourceLoader = new FileSystemResourceLoader()
            processDataClient.findOneByName('process') >> new Process(id: 1, name: 'process')

            String actual = processCommands.update('process', tempFile.absolutePath)

        then:
            1 * processDataClient.save({ Process it ->
                it.name == 'process' && it.definition == 'definition' && it.lastUpdate != null
            }) >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time')

        expect:
            actual ==   'id:                 1\n' +
                        'name:               process\n' +
                        'last update:        time\n' +
                        'definition:\n' +
                        'definition'
    }

    def '`process add` command'() {
        given:
            File tempFile = File.createTempFile('diploexec-shell-test', null)
            tempFile.text = 'definition'

        when:
            processCommands.resourceLoader = new FileSystemResourceLoader()

            String actual = processCommands.add('process', tempFile.absolutePath)

        then:
            1 * processDataClient.save({ Process it ->
                it.name == 'process' && it.definition == 'definition' && it.lastUpdate != null
            }) >> new Process(id: 1, name: 'process', definition: 'definition', lastUpdate: 'time')

        expect:
        actual ==   'id:                 1\n' +
                    'name:               process\n' +
                    'last update:        time\n' +
                    'definition:\n' +
                    'definition'
    }
}
