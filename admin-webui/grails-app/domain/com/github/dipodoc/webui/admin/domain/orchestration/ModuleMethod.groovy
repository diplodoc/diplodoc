package com.github.dipodoc.webui.admin.domain.orchestration

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class ModuleMethod {

    static mapWith = 'mongo'

    ObjectId id


    String name

    static belongsTo = [ module: Module ]


    static mapping = {
        version false
    }
}
