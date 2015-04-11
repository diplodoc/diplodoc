package com.github.diplodoc.diplobase.repository.mongodb.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface DocRepository extends MongoRepository<Doc, String> {

    Doc findOneByUrl(String url)

    Collection<Doc> findByTrainMeaningHtmlIsNotNull()
}
