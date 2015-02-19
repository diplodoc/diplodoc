package com.github.diplodoc.diplobase.repository.jpa.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * @author yaroslav.yermilov
 */
interface ProcessRunRepository extends PagingAndSortingRepository<ProcessRun, Long> { }
