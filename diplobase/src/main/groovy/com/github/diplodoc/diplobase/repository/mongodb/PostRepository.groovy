package com.github.diplodoc.diplobase.repository.mongodb

import com.github.diplodoc.diplobase.domain.mongodb.Post
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * @author yaroslav.yermilov
 */
interface PostRepository extends PagingAndSortingRepository<Post, String> {

    Post findOneByUrl(String url)
}
