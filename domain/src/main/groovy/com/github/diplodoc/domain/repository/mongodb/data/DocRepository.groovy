package com.github.diplodoc.domain.repository.mongodb.data

import com.github.diplodoc.domain.mongodb.data.Doc
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface DocRepository extends MongoRepository<Doc, String> {

    Doc findOneByUri(String uri)

    List findBySourceIdIn(Collection sourceIds, Pageable pageable)
}
