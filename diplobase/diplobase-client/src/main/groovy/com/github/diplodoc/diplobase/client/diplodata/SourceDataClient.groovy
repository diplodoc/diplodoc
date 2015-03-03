package com.github.diplodoc.diplobase.client.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.SourceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class SourceDataClient {

    @Autowired
    SourceRepository sourceRepository

    Collection<Source> all() {
        sourceRepository.findAll()
    }

    Source byName(String name) {
        sourceRepository.findOneByName(name)
    }

    Source save(Source source) {
        sourceRepository.save(source)
    }
}
