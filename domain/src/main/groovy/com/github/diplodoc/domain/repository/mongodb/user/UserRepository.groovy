package com.github.diplodoc.domain.repository.mongodb.user

import com.github.diplodoc.domain.mongodb.user.User
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * @author yaroslav.yermilov
 */
interface UserRepository extends MongoRepository<User, String> {

    User findOneByGoogleId(String googleId)
}
