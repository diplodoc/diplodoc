package com.github.diplodoc.diplobase.domain.mongodb.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class Process {

    @Id
    String id


    String name

    String definition

    String lastUpdate

    boolean active
}
