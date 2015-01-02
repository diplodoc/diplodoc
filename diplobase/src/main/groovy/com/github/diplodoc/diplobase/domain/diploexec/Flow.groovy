package com.github.diplodoc.diplobase.domain.diploexec

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

/**
 * @author yaroslav.yermilov
 */
@Entity
@Table(schema = 'diploexec')
class Flow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String name

    String definition

    @Transient
    List<String> listensTo

    @Transient
    List<String> waitingFor
}
