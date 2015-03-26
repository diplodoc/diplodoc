package com.github.dipodoc.diploweb.diploexec

import groovy.transform.EqualsAndHashCode

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

        id generator: 'sequence', params: [sequence:'diploexec.process_id_seq']
        lastUpdate column: 'lastupdate'
    }
}
