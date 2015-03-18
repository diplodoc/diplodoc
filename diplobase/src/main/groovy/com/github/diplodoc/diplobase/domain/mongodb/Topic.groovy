package com.github.diplodoc.diplobase.domain.mongodb

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.mongodb.core.mapping.DBRef

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString(includes = 'label')
class Topic {

    String id

    String label

    @DBRef
    Topic parent
}
