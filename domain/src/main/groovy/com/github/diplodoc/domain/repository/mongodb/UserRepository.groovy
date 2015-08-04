package com.github.diplodoc.domain.repository.mongodb

import com.github.diplodoc.domain.mongodb.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface UserRepository extends MongoRepository<User, ObjectId> {

    User findOneByGoogleId(String googleId)

    User findOneByGoogleSubject(String googleSubject)
}
