package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.client.diplodata.SourceDataClient
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SourcesCommandsSpec extends Specification {

    SourceDataClient sourceDataClient = Mock(SourceDataClient)
    SourcesCommands sourcesCommands = new SourcesCommands(sourceDataClient: sourceDataClient)

    def '`sources list` command'() {
        when:
            sourceDataClient.findAll() >> [
                new Source(id: 1, name: 'name-1', newPostsFinderModule: 'module-1'),
                new Source(id: 2, name: 'name-2', newPostsFinderModule: 'module-2')
            ]

        then:
            String actual = sourcesCommands.list()

        expect:
            actual == '    1                        name-1                                          module-1\n' +
                      '    2                        name-2                                          module-2'
    }

    def '`sources get --representation text` command'() {
        when:
            sourceDataClient.findOneByName('name') >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module')

        then:
            String actual = sourcesCommands.get('name', 'text')

        expect:
            actual ==   'id:                           1\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module'
    }

    def '`sources get --representation json` command'() {
        when:
            sourceDataClient.findOneByName('name') >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module')

        then:
            String actual = sourcesCommands.get('name', 'json')

        expect:
            actual == '{"type":"com.github.diplodoc.diplobase.domain.diplodata.Source","id":1,"newPostsFinderModule":"module","name":"name"}'
    }
}
