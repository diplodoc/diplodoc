package com.github.diplodoc.domain.repository.mongodb.data

import com.github.diplodoc.domain.mongodb.data.Doc
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface DocRepository extends MongoRepository<Doc, ObjectId> {

    Doc findOneByUri(String uri)

    Collection<Doc> findByTrainMeaningHtmlIsNotNull()

    Collection<Doc> findByKnu(String knu)

    List<Doc> findByKnuIsNull(Pageable pageable)
}
