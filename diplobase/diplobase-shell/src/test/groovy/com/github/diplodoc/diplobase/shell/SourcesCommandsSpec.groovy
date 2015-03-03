package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.client.diplodata.SourceDataClient
import com.github.diplodoc.diplobase.domain.mongodb.Source
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SourcesCommandsSpec extends Specification {

    SourceDataClient sourceDataClient = Mock(SourceDataClient)
    SourcesCommands sourcesCommands = new SourcesCommands(sourceDataClient: sourceDataClient)

    def 'sources list'() {
        when:
            sourceDataClient.all() >> [
                new Source(id: 1, name: 'name-1', newPostsFinderModule: 'module-1', rssUrl: 'rss-url-1'),
                new Source(id: 2, name: 'name-2', newPostsFinderModule: 'module-2', rssUrl: 'rss-url-2')
            ]

        then:
            String actual = sourcesCommands.list()

        expect:
            actual == '    1                        name-1\n' +
                      '    2                        name-2'
    }

    def 'sources get --representation text'() {
        when:
            sourceDataClient.byName('name') >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        then:
            String actual = sourcesCommands.get('name', 'text')

        expect:
            actual ==   'id:                           1\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module\n' +
                        'rss url:                      rss-url'
    }

    def 'sources get --representation json'() {
        when:
            sourceDataClient.byName('name') >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        then:
            String actual = sourcesCommands.get('name', 'json')

        expect:
            actual == '{"type":"com.github.diplodoc.diplobase.domain.mongodb.Source","id":1,"newPostsFinderModule":"module","rssUrl":"rss-url","name":"name"}'
    }

    def 'sources add name --new-post-finder-module module --rss-url rss-url'() {
        when:
            String actual = sourcesCommands.add('name', 'module', 'rss-url')

        then:
            1 * sourceDataClient.save(new Source(name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')) >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        expect:
            actual ==   'id:                           1\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module\n' +
                        'rss url:                      rss-url'
    }

    def 'sources update name --new-post-finder-module module'() {
        when:
            sourceDataClient.byName('name') >> new Source(id: 1, name: 'name')

            String actual = sourcesCommands.update('name', 'module', null)

        then:
            1 * sourceDataClient.save(new Source(id: 1, name: 'name', newPostsFinderModule: 'module')) >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module')

        expect:
            actual ==   'id:                           1\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module\n' +
                        'rss url:                      null'
    }

    def 'sources update name --rss-url rss-url'() {
        when:
            sourceDataClient.byName('name') >> new Source(id: 1, name: 'name')

            String actual = sourcesCommands.update('name', null, 'rss-url')

        then:
            1 * sourceDataClient.save(new Source(id: 1, name: 'name', rssUrl: 'rss-url')) >> new Source(id: 1, name: 'name', rssUrl: 'rss-url')

        expect:
            actual ==   'id:                           1\n' +
                        'name:                         name\n' +
                        'new posts finder module:      null\n' +
                        'rss url:                      rss-url'
    }

    def 'sources update name --new-post-finder-module module --rss-url rss-url'() {
        when:
            sourceDataClient.byName('name') >> new Source(id: 1, name: 'name')

            String actual = sourcesCommands.update('name', 'module', 'rss-url')

        then:
            1 * sourceDataClient.save(new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')) >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        expect:
            actual ==   'id:                           1\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module\n' +
                        'rss url:                      rss-url'
    }
}
