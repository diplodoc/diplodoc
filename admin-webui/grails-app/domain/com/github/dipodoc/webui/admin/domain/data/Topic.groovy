package com.github.dipodoc.webui.admin.domain.data

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

    static mapping = {
        version false
    }
}
