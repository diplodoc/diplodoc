package com.github.diplodoc.domain.mongodb.user

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class User {

    @Id
    String id

    String googleId
}
