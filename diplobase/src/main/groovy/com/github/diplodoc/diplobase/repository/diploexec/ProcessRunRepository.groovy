package com.github.diplodoc.diplobase.repository.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRunRepository extends PagingAndSortingRepository<ProcessRun, Long> { }
