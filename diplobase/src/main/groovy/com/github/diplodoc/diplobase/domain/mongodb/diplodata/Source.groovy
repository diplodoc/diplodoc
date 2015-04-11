package com.github.diplodoc.diplobase.domain.mongodb.diplodata

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

    String newDocsFinderModule

    String rssUrl
}
