package com.github.diplodoc.domain.mongodb

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class User {

    @Id
    ObjectId id

    String googleId

    String googleSubject
}
