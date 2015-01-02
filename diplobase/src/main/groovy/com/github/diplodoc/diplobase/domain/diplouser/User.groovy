package com.github.diplodoc.diplobase.domain.diplouser

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

/**
 * @author yaroslav.yermilov
 */
@Entity
@Table(schema = 'diplouser')
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String name

    @ManyToMany
    List<Interest> interests
}
