package com.github.diplodoc.domain.mongodb.orchestration

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode(excludes = 'parameters')
@ToString
class ProcessRun {

    enum EXIT_STATUSES { UNKNOWN, NOT_FINISHED, SUCCEED, FAILED  }

    @Id
    ObjectId id


    @Field('process')
    ObjectId processId

    String startTime

    String endTime

    String exitStatus = EXIT_STATUSES.UNKNOWN

    String errorMessage

    Collection<ProcessRunParameter> parameters
}
