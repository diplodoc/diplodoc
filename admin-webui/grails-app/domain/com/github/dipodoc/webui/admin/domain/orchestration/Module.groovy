package com.github.dipodoc.webui.admin.domain.orchestration

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Module {

    static mapWith = 'mongo'

    ObjectId id


    String name

    Map data


    static mapping = {
        version false
    }
}
