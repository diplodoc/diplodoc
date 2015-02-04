package com.github.diplodoc.diplobase.client.diplodata

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class SourceDataClient {

    @Autowired
    SourceRepository sourceRepository

    Iterable<Source> findAll() {
        sourceRepository.findAll()
    }

    Source findOneByName(String name) {
        sourceRepository.findOneByName(name)
    }
}
