package com.github.diplodoc.diplobase.repository.mongodb.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethod
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ModuleMethodRepository extends MongoRepository<ModuleMethod, String> {

    Collection<ModuleMethod> findByName(String name)
}