package com.github.diplodoc.domain.mongodb.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class Source {

    @Id
    ObjectId id


    String name

    String rssUrl
}
