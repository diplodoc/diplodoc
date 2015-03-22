package com.github.dipodoc.diploweb.diploexec

class ProcessRunParameter {

    Long id

    String key

    String type

    String value

    static belongsTo = [ processRun: ProcessRun ]

    static mapping = {
        table schema: 'diploexec', name: 'processrunparameter'

        version false

        id generator: 'sequence', params: [sequence:'diploexec.processrunparameter_id_seq']
        processRun column: 'processrun_id'
    }
}
