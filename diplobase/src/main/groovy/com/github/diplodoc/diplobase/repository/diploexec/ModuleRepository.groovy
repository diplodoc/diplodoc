package com.github.diplodoc.diplobase.repository.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Module
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

/**
 * @author yaroslav.yermilov
 */
interface ModuleRepository extends CrudRepository<Module, Long> {

    Module findOneByName(@Param('name') String name)
}