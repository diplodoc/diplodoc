package com.github.dipodoc.diploweb.domain.diplodata

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Field

@EqualsAndHashCode
class Doc {

    static mapWith = 'mongo'

    ObjectId id


    String uri

    String type

    static belongsTo = [ source: Source ]

    String loadTime


    String title

    String description

    String publishTime

    String html


    String train_meaningHtml

    String meaningHtml

    String meaningText


    static hasMany = [ train_topics: Topic ]

    List predicted_topics


    boolean knu_document


    static constraints = {
        uri nullable: true
        type nullable: true
        source nullable: true
        loadTime nullable: true
        title nullable: true
        description nullable: true
        publishTime nullable: true
        html nullable: true
        train_meaningHtml nullable: true
        meaningHtml nullable: true
        meaningText nullable: true
        predicted_topics nullable: true
    }

    static mapping = {
        version false
    }
}
