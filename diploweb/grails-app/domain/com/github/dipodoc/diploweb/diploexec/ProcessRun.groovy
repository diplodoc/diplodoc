package com.github.dipodoc.diploweb.diploexec

class ProcessRun {

    Long id

    static belongsTo = [ process: Process ]

    static hasMany = [ parameters: ProcessRunParameter ]

    String startTime

    String endTime

    String exitStatus

    static mapping = {
        table schema: 'diploexec', name: 'processrun'

        version false

        id generator: 'sequence', params: [sequence:'diploexec.processrun_id_seq']
        startTime column: 'starttime'
        endTime column: 'endtime'
        exitStatus column: 'exitstatus'
    }
}
