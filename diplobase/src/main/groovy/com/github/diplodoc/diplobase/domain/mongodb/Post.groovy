package com.github.diplodoc.diplobase.domain.mongodb

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.mongodb.core.mapping.DBRef

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString(excludes = [ 'meaningText', 'html' ])
class Post {

    String id

    String url

    @DBRef
    Source source

    String loadTime


    String html

    String title

    String description

    String publishTime


    String meaningText

    @DBRef
    List predicted_topics


    @DBRef
    Collection<Topic> train_topics
}
