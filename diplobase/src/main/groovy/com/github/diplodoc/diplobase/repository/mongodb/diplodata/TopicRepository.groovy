package com.github.diplodoc.diplobase.repository.mongodb.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Topic
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface TopicRepository extends MongoRepository<Topic, ObjectId> {

    Topic findOneByLabel(String label)
}