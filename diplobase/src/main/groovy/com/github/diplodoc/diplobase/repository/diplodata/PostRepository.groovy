package com.github.diplodoc.diplobase.repository.diplodata

import com.github.diplodoc.diplobase.domain.diplodata.Post
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * @author yaroslav.yermilov
 */
interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
