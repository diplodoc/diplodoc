package com.github.diplodoc.diplobase.repository.mongodb

import com.github.diplodoc.diplobase.domain.mongodb.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

/**
 * @author yaroslav.yermilov
 */
interface PostRepository extends MongoRepository<Post, String> {

    Post findOneByUrl(String url)

    Collection<Post> findByTrainMeaningHtmlIsNotNull()
}
