package com.github.dipodoc.diploweb.domain.diploexec

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class ModuleMethod {

    static mapWith = 'mongo'

    ObjectId id


    String name

    static belongsTo = [ module: Module ]
}
