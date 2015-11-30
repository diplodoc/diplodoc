package com.github.diplodoc.domain.mongodb.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class Doc {

    @Id
    String id


    String uri

    String sourceId


    String html


    String title

    String publishTime
}
