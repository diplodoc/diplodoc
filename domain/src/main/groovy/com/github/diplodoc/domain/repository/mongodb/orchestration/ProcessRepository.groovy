package com.github.diplodoc.domain.repository.mongodb.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRepository extends MongoRepository<Process, ObjectId> {

    Collection<Process> findByActiveIsTrue()
}