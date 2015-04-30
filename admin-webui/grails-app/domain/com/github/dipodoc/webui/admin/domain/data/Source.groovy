package com.github.dipodoc.webui.admin.domain.data

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Source {

    static mapWith = 'mongo'

    ObjectId id


    String name

    String newDocsFinderModule

    String rssUrl


    static mapping = {
        version false
    }
}
