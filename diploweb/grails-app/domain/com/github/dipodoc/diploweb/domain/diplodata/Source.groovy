package com.github.dipodoc.diploweb.domain.diplodata

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Source {

    static mapWith = 'mongo'

    ObjectId id


    String name

    String newDocsFinderModule

    String rssUrl
}
