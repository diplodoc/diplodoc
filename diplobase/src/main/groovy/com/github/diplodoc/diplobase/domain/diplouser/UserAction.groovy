package com.github.diplodoc.diplobase.domain.diplouser

import com.github.diplodoc.diplobase.domain.diplodata.Post

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
@Table(schema = 'diplouser')
class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    ActionType type

    @ManyToOne
    User user

    @ManyToOne
    Post post
}
