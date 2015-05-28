package com.github.diplodoc.domain.repository.mongodb.data

import com.github.diplodoc.domain.mongodb.data.Topic
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface TopicRepository extends MongoRepository<Topic, ObjectId> { }