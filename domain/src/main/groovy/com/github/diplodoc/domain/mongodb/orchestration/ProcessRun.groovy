package com.github.diplodoc.domain.mongodb.orchestration

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode(excludes = 'parameters')
@ToString
class ProcessRun {

    enum EXIT_STATUSES { UNKNOWN, NOT_FINISHED, SUCCEED, FAILED  }

    @Id
    String id


    String processId

    String startTime

    String endTime

    String exitStatus = EXIT_STATUSES.UNKNOWN

    String errorMessage

    Collection<ProcessRunParameter> parameters
}
