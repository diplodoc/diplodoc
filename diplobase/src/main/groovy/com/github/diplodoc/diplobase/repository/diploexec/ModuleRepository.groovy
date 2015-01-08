package com.github.diplodoc.diplobase.repository.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Process
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/**
 * @author yaroslav.yermilov
 */
interface ProcessRepository extends CrudRepository<Process, Long> {

    Process findOneByName(@Param('name') String name)
}