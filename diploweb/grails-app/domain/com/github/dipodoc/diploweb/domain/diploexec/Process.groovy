package com.github.dipodoc.diploweb.domain.diploexec

import groovy.transform.EqualsAndHashCode

import java.time.LocalDateTime

@EqualsAndHashCode
class Process {

    Long id

    String name

    String definition

    String lastUpdate

    boolean active

    static mapping = {
        table schema: 'diploexec'

        version false

        id generator: 'sequence', params: [ sequence:'diploexec.process_id_seq' ]
        lastUpdate column: 'lastupdate'
    }

    def beforeInsert() {
        updatelastUpdateTime()
    }

    def beforeUpdate() {
        updatelastUpdateTime()
    }

    def updatelastUpdateTime() {
        lastUpdate = LocalDateTime.now().toString()
    }
}
