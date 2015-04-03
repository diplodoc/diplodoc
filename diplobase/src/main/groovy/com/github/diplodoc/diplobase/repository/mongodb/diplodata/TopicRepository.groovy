package com.github.diplodoc.diplobase.repository.mongodb.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Topic
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface TopicRepository extends MongoRepository<Topic, String> {

    Topic findOneByLabel(String label)
}