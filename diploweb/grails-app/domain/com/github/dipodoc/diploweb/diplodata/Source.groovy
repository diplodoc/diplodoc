package com.github.dipodoc.diploweb.diplodata

import org.bson.types.ObjectId

class Source {

    static mapWith = 'mongo'

    ObjectId id


    String name

    String newPostsFinderModule

    String rssUrl
}
