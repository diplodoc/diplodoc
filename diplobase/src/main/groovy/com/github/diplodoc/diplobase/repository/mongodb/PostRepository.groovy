package com.github.diplodoc.diplobase.repository.mongodb

import com.github.diplodoc.diplobase.domain.mongodb.Post
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface PostRepository extends MongoRepository<Post, String> {

    String findOneByUrl(String url)
}
