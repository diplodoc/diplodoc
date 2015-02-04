package com.github.diplodoc.diplobase.client.diplodata

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
import spock.lang.Specification


/**
 * @author yaroslav.yermilov
 */
class SourceDataClientSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)
    SourceDataClient sourceDataClient = new SourceDataClient(sourceRepository: sourceRepository)

    def 'Iterable<Source> findAll()'() {
        when:
        def actual = sourceDataClient.findAll()

        then:
        1 * sourceRepository.findAll() >> [
                new Source(id: 1, name: 'name-1', newPostsFinderModule: 'module-1'),
                new Source(id: 2, name: 'name-2', newPostsFinderModule: 'module-2')
        ]

        expect:
        actual.size() == 2
        actual[0] == new Source(id: 1, name: 'name-1', newPostsFinderModule: 'module-1')
        actual[1] == new Source(id: 2, name: 'name-2', newPostsFinderModule: 'module-2')
    }

    def 'Source findOneByName(String name)'() {
        when:
            Source actual = sourceDataClient.findOneByName('name')

        then:
            1 * sourceRepository.findOneByName('name') >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module')

        expect:
            actual == new Source(id: 1, name: 'name', newPostsFinderModule: 'module')
    }
}
