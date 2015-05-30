package com.github.dipodoc.webui.admin.domain.orchestration

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class ModuleMethodRun {

    static mapWith = 'mongo'

    ObjectId id


    String startTime

    String endTime

    Map parameters

    Map metrics

    static belongsTo = [ moduleMethod: ModuleMethod ]


    static mapping = {
        version false
    }
}
