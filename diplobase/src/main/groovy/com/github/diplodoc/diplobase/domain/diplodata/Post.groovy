package com.github.diplodoc.diplobase.domain.diplodata

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * @author yaroslav.yermilov
 */
@Entity
@Table(schema = 'diplodata')
class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String url

    @ManyToOne
    Source source;

    String html

    String title

    String meaningText
}
