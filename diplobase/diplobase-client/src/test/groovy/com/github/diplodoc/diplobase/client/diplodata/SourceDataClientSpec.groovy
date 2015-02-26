package com.github.diplodoc.diplobase.client.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.SourceRepository
import spock.lang.Specification


/**
 * @author yaroslav.yermilov
 */
class SourceDataClientSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)
    SourceDataClient sourceDataClient = new SourceDataClient(sourceRepository: sourceRepository)

    def 'Collection<Source> all()'() {
        when:
            def actual = sourceDataClient.all()

        then:
            1 * sourceRepository.findAll() >> [
                new Source(id: 1, name: 'name-1', newPostsFinderModule: 'module-1', rssUrl: 'rss-url-1'),
                new Source(id: 2, name: 'name-2', newPostsFinderModule: 'module-2', rssUrl: 'rss-url-2')
            ]

        expect:
            actual.size() == 2
            actual[0] == new Source(id: 1, name: 'name-1', newPostsFinderModule: 'module-1', rssUrl: 'rss-url-1')
            actual[1] == new Source(id: 2, name: 'name-2', newPostsFinderModule: 'module-2', rssUrl: 'rss-url-2')
    }

    def 'Source byName(String name)'() {
        when:
            Source actual = sourceDataClient.byName('name')

        then:
            1 * sourceRepository.findOneByName('name') >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        expect:
            actual == new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')
    }

    def 'Source save(Source source)'() {
        when:
            Source actual = sourceDataClient.save(new Source(name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url'))

        then:
            1 * sourceRepository.save(new Source(name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')) >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')

        expect:
            actual == new Source(id: 1, name: 'name', newPostsFinderModule: 'module', rssUrl: 'rss-url')
    }
}
