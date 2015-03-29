package com.github.dipodoc.diploweb.diplodata

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Post {

    static mapWith = 'mongo'

    ObjectId id


    String url

    static belongsTo = [ source: Source ]

    String loadTime


    String title

    String description

    String publishTime

    String html


    String train_meaningHtml

    String meaningText


    static hasMany = [ train_topics: Topic ]

    List predicted_topics
}
