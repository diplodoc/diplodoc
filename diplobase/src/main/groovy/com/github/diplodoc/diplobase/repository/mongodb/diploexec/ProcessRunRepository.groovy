package com.github.diplodoc.diplobase.repository.mongodb.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRunRepository extends MongoRepository<ProcessRun, String> { }
