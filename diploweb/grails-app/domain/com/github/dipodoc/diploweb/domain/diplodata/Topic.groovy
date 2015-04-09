package com.github.dipodoc.diploweb.domain.diplodata

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Topic {

    static mapWith = 'mongo'

    ObjectId id


    String label

    static belongsTo = [ parent: Topic ]

    static constraints = {
        parent nullable: true
    }
}
