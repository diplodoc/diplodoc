package com.github.diplodoc.diplobase.domain.mongodb.diplodata

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class Doc {

    @Id
    ObjectId id


    String uri

    String type

    @Field('source')
    ObjectId sourceId

    String loadTime


    String html

    byte[] binary


    String title

    String description

    String publishTime


    @Field('train_meaningHtml')
    String trainMeaningHtml

    String meaningHtml

    String meaningText


    @Field('knu_document')
    boolean knuDocument
}
