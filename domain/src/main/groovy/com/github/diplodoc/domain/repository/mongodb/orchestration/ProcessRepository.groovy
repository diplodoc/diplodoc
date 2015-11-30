package com.github.diplodoc.domain.repository.mongodb.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRepository extends MongoRepository<Process, String> {

    Collection<Process> findByActiveIsTrue()

    Process findOneByNameAndActiveIsTrue(String name)
}