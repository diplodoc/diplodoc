package com.github.diplodoc.diplobase.repository.mongodb.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ModuleRepository extends MongoRepository<Module, ObjectId> {

    Module findOneByName(String name)
}
