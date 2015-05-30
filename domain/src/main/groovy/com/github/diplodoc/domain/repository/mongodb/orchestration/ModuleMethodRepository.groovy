package com.github.diplodoc.domain.repository.mongodb.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ModuleMethod
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ModuleMethodRepository extends MongoRepository<ModuleMethod, ObjectId> {

    Collection<ModuleMethod> findByName(String name)
}