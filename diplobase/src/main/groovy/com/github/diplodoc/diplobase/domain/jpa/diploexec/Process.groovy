package com.github.diplodoc.diplobase.domain.jpa.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Entity
@Table(schema = 'diploexec')
@EqualsAndHashCode
@ToString(excludes = 'definition')
class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String name

    String definition

    LocalDateTime lastUpdate

    boolean active
}
