package com.github.diplodoc.domain.mongodb.orchestration

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
class ModuleMethodRun {

    @Id
    ObjectId id


    String startTime

    String endTime

    Map parameters

    Map metrics

    @Field('moduleMethod')
    ObjectId moduleMethodId
}
