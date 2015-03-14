package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.SourceRepository
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SourcesCommandsSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)

    SourcesCommands sourcesCommands = new SourcesCommands(sourceRepository: sourceRepository)

    def '`sources list`'() {
        when:
            sourceRepository.findAll() >> [
                new Source(name: 'name-1'),
                new Source(name: 'name-2')
            ]

        then:
            String actual = sourcesCommands.list()

        expect:
            actual == '                        name-1\n' +
                      '                        name-2'
    }

    def '`sources add name --new-post-finder-module module --rss-url rss-url`'() {
        when:
            String actual = sourcesCommands.add('name', 'module', 'rss-url')

        then:
            1 * sourceRepository.save(new Source(name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')) >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        expect:
            actual ==   'id:                           id\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module\n' +
                        'rss url:                      rss-url'
    }

    def '`sources update name --new-post-finder-module module`'() {
        when:
            sourceRepository.findOneByName('name') >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'old-module', rssUrl: 'old-rss-url')

            String actual = sourcesCommands.update('name', 'new-module', null)

        then:
            1 * sourceRepository.save(new Source(id: 'id', name: 'name', newPostsFinderModule: 'new-module', rssUrl: 'old-rss-url')) >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'new-module', rssUrl: 'old-rss-url')

        expect:
            actual ==   'id:                           id\n' +
                        'name:                         name\n' +
                        'new posts finder module:      new-module\n' +
                        'rss url:                      old-rss-url'
    }

    def '`sources update name --rss-url rss-url`'() {
        when:
            sourceRepository.findOneByName('name') >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'old-module', rssUrl: 'old-rss-url')

            String actual = sourcesCommands.update('name', null, 'new-rss-url')

        then:
            1 * sourceRepository.save(new Source(id: 'id', name: 'name', newPostsFinderModule: 'old-module', rssUrl: 'new-rss-url')) >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'old-module', rssUrl: 'new-rss-url')

        expect:
            actual ==   'id:                           id\n' +
                        'name:                         name\n' +
                        'new posts finder module:      old-module\n' +
                        'rss url:                      new-rss-url'
    }

    def '`sources update name --new-post-finder-module module --rss-url rss-url`'() {
        when:
            sourceRepository.findOneByName('name') >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'old-module', rssUrl: 'old-rss-url')

            String actual = sourcesCommands.update('name', 'new-module', 'new-rss-url')

        then:
            1 * sourceRepository.save(new Source(id: 'id', name: 'name', newPostsFinderModule: 'new-module', rssUrl: 'new-rss-url')) >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'new-module', rssUrl: 'new-rss-url')

        expect:
            actual ==   'id:                           id\n' +
                        'name:                         name\n' +
                        'new posts finder module:      new-module\n' +
                        'rss url:                      new-rss-url'
    }

    def '`sources get --representation text`'() {
        when:
            sourceRepository.findOneByName('name') >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        then:
            String actual = sourcesCommands.get('name', 'text')

        expect:
            actual ==   'id:                           id\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module\n' +
                        'rss url:                      rss-url'
    }

    def '`sources get --representation json`'() {
        when:
            sourceRepository.findOneByName('name') >> new Source(id: 'id', name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        then:
            String actual = sourcesCommands.get('name', 'json')

        expect:
            actual == '{"_type":"com.github.diplodoc.diplobase.domain.mongodb.Source","id":"id","newPostsFinderModule":"module","rssUrl":"rss-url","name":"name"}'
    }

    def 'static String toSingleLineDescription(Source source)'() {
        when:
            Source source = new Source(id: 'id', name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        then:
            String actual = SourcesCommands.toSingleLineDescription(source)

        expect:
            actual == '                          name'
    }

    def 'static String toDescription(Source source)'() {
        when:
            Source source = new Source(id: 'id', name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        then:
            String actual = SourcesCommands.toDescription(source)

        expect:
            actual ==   'id:                           id\n' +
                        'name:                         name\n' +
                        'new posts finder module:      module\n' +
                        'rss url:                      rss-url'
    }

    def 'static String toJson(Source source)'() {
        when:
            Source source = new Source(id: 'id', name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        then:
            String actual = SourcesCommands.toJson(source)

        expect:
            actual == '{"_type":"com.github.diplodoc.diplobase.domain.mongodb.Source","id":"id","newPostsFinderModule":"module","rssUrl":"rss-url","name":"name"}'
    }
}
