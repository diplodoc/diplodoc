package com.github.diplodoc.diplobase.domain.mongodb.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode(excludes = 'parameters')
@ToString
class ProcessRun {

    @Id
    String id


    @DBRef
    Process process

    String startTime

    String endTime

    String exitStatus

    Collection<ProcessRunParameter> parameters
}
