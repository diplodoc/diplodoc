package com.github.diplodoc.diplobase.domain.mongodb

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class Source {

    @Id
    String id

    String name

    String newPostsFinderModule

    String rssUrl
}
