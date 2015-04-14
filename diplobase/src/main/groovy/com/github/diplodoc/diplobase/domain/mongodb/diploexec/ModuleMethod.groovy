package com.github.diplodoc.diplobase.domain.mongodb.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class ModuleMethod {

    @Id
    ObjectId id


    String name

    @Field('module')
    ObjectId moduleId
}
