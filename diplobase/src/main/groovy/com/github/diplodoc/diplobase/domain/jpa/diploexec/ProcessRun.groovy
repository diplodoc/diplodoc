package com.github.diplodoc.diplobase.domain.jpa.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Entity
@Table(schema = 'diploexec')
@EqualsAndHashCode
@ToString
class ProcessRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @ManyToOne
    Process process

    @OneToMany(cascade = CascadeType.ALL, mappedBy = 'processRun', fetch = FetchType.EAGER)
    Collection<ProcessRunParameter> parameters

    LocalDateTime startTime

    LocalDateTime endTime

    String exitStatus
}
