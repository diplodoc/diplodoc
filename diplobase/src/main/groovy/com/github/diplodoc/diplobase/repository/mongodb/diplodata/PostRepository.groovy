package com.github.diplodoc.diplobase.repository.mongodb.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Post
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface PostRepository extends MongoRepository<Post, String> {

    Post findOneByUrl(String url)

    Collection<Post> findByTrainMeaningHtmlIsNotNull()
}
