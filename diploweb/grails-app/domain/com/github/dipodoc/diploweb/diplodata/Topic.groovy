package com.github.dipodoc.diploweb.diplodata

import org.bson.types.ObjectId

class Topic {

    static mapWith = 'mongo'

    ObjectId id


    String label

    static belongsTo = [ parent: Topic ]
}
