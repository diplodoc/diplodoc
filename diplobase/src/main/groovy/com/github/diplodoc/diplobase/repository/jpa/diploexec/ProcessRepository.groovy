package com.github.diplodoc.diplobase.repository.jpa.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import org.springframework.data.repository.CrudRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRepository extends CrudRepository<Process, Long> {

    Process findOneByName(String name)

    Collection<Process> findByActiveIsTrue()

    Collection<Process> findByNameLike(String pattern)
}