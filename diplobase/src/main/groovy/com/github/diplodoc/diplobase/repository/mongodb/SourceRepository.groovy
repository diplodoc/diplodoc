package com.github.diplodoc.diplobase.repository.mongodb

import com.github.diplodoc.diplobase.domain.mongodb.Source
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * @author yaroslav.yermilov
 */
interface SourceRepository extends PagingAndSortingRepository<Source, String> {

    Source findOneByName(String name)
}