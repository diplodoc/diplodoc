package com.github.diplodoc.domain.mongodb.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString(includes = 'label')
class Topic {

    @Id
    ObjectId id


    String label

    @Field('parent')
    ObjectId parentId
}
