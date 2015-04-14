package com.github.diplodoc.diplobase.domain.mongodb.diplodata

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString(includes = 'label')
class Topic {

    @Id
    String id

    String label

    @DBRef
    Topic parent
}