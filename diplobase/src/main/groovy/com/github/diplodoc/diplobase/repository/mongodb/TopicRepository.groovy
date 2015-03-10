package com.github.diplodoc.diplobase.repository.mongodb

import com.github.diplodoc.diplobase.domain.mongodb.Topic
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface TopicRepository extends MongoRepository<Topic, String> {

    Topic findOneByLabel(String label)
}