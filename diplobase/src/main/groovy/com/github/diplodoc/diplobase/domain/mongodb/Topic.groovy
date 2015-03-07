package com.github.diplodoc.diplobase.domain.mongodb

import groovy.transform.EqualsAndHashCode
import org.springframework.data.mongodb.core.mapping.DBRef

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
class Topic {

    String id

    String label

    @DBRef
    Topic parent
}
