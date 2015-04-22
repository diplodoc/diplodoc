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

    String lastUpdate

    boolean active


    static constraints = {
        lastUpdate nullable: true
    }

    static mapping = {
        version false
    }


    def beforeInsert() {
        updateFields()
    }

    def beforeUpdate() {
        if (isDirty('name') || isDirty('definition')) {
            updateFields()
        }
    }

    protected void updateFields() {
        definition = definition.replace('\r', '')
        lastUpdate = LocalDateTime.now()
    }
}
