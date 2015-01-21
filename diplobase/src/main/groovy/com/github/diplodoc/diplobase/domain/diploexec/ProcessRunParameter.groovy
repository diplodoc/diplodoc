package com.github.diplodoc.diplobase.domain.diploexec

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
@Table(schema = 'diploexec')
class ProcessRunParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String key

    String type

    String value

    @ManyToOne
    ProcessRun processRun
}
