package com.github.diplodoc.diplobase.domain.mongodb

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Field

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString(excludes = [ 'meaningText', 'html' ])
class Post {

    @Id
    String id


    String url

    @DBRef
    Source source

    String loadTime


    String title

    String description

    String publishTime

    String html


    @Field('train_meaningHtml')
    String trainMeaningHtml

    String meaningText


    @DBRef
    Collection<Topic> train_topics
}
