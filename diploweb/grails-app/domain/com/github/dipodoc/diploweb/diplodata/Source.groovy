package com.github.dipodoc.diploweb.diplodata

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Source {

    static mapWith = 'mongo'

    ObjectId id


    String name

    String newPostsFinderModule

    String rssUrl
}
