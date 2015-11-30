package com.github.diplodoc.domain.repository.mongodb.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRunRepository extends MongoRepository<ProcessRun, String> {}
