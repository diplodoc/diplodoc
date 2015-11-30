package com.github.diplodoc.domain.repository.mongodb.data

import com.github.diplodoc.domain.mongodb.data.Source
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface SourceRepository extends MongoRepository<Source, String> { }