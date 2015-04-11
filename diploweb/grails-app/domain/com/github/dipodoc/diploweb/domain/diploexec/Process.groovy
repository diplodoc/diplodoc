package com.github.dipodoc.diploweb.domain.diploexec

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

import java.time.LocalDateTime

@EqualsAndHashCode
class Process {

    static mapWith = 'mongo'

    ObjectId id


    String name

    String definition

    LocalDateTime lastUpdate

    boolean active


    def beforeInsert() {
        updateLastUpdateTime()
    }

    def beforeUpdate() {
        updateLastUpdateTime()
    }

    def updateLastUpdateTime() {
        lastUpdate = LocalDateTime.now()
    }
}
