package com.github.dipodoc.diploweb.domain.diploexec

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

import java.time.LocalDateTime

@EqualsAndHashCode
class ModuleMethodRun {

    static mapWith = 'mongo'

    ObjectId id


    LocalDateTime startTime

    LocalDateTime endTime

    Map metrics

    static belongsTo = [ moduleMethod: ModuleMethod ]
}
