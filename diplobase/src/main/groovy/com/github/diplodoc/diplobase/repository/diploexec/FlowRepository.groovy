package com.github.diplodoc.diplobase.repository.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Flow
import org.springframework.data.repository.CrudRepository

/**
 * @author yaroslav.yermilov
 */
interface FlowRepository extends CrudRepository<Flow, Long> {

    Flow findOneByName(String name)
}