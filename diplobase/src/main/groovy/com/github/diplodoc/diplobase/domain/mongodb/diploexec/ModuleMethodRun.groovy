package com.github.diplodoc.diplobase.domain.mongodb.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Field

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class ModuleMethodRun {

    @Id
    ObjectId id


    LocalDateTime startTime

    LocalDateTime endTime

    Map metrics

    @Field('moduleMethod')
    ObjectId moduleMethodId
}
