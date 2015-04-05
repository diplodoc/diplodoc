package com.github.diplodoc.diplocore.modules.data

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.SourceRepository
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SourcesSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)

    Sources sources = new Sources(sourceRepository: sourceRepository)

    def 'Collection<Source> all()'() {
        when:
            sourceRepository.findAll() >> [ new Source(name: 'name-1'), new Source(name: 'name-2') ]

        then:
            Collection<Source> actual = sources.all()

        expect:
            actual == [ new Source(name: 'name-1'), new Source(name: 'name-2') ]
    }
}
