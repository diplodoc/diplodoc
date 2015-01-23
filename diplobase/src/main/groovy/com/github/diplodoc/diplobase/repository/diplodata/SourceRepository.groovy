package com.github.diplodoc.diplobase.repository.diplodata

import com.github.diplodoc.diplobase.domain.diplodata.Source
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * @author yaroslav.yermilov
 */
interface SourceRepository extends PagingAndSortingRepository<Source, Long> {

    Source findOneByName(String name)
}