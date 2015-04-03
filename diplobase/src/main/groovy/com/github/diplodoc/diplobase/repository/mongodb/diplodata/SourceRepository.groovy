package com.github.diplodoc.diplobase.repository.mongodb.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface SourceRepository extends MongoRepository<Source, String> {

    Source findOneByName(String name)
}