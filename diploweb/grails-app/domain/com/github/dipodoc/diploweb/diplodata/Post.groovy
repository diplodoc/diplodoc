package com.github.dipodoc.diploweb.diplodata

import org.bson.types.ObjectId

class Post {

    static mapWith = 'mongo'

    ObjectId id


    String url

    String loadTime

    String title

    String description

    String publishTime

    static belongsTo = [ source: Source ]
}
