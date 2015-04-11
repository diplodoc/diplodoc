package com.github.diplodoc.diplobase.repository.mongodb.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Process
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRepository extends MongoRepository<Process, String> {

    Process findOneByName(String name)

    Collection<Process> findByActiveIsTrue()
}