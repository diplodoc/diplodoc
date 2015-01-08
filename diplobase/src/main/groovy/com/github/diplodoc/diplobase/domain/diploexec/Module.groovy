package com.github.diplodoc.diplobase.domain.diploexec

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient
import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Entity
@Table(schema = 'diploexec')
class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String name

    String definition

    String lastUpdate

    @Transient
    List<String> listensTo

    @Transient
    List<String> waitingFor
}
