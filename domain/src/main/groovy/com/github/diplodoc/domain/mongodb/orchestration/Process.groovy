package com.github.diplodoc.domain.mongodb.orchestration

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

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

    String lastUpdate

    boolean active
}
