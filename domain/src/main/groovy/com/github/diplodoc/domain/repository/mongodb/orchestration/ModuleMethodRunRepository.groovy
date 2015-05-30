package com.github.diplodoc.domain.repository.mongodb.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ModuleMethodRun
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ModuleMethodRunRepository extends MongoRepository<ModuleMethodRun, ObjectId> {}