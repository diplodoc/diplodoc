package com.github.diplodoc.diplobase.domain.mongodb.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class Process {

    @Id
    ObjectId id


    String name

    String definition

    LocalDateTime lastUpdate

    boolean active
}
