package com.github.diplodoc.diplobase.repository.mongodb.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ModuleMethodRunRepository extends MongoRepository<ModuleMethodRun, String> {}