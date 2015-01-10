package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.client.ProcessRunDataClient
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class Diploexec {

    @Autowired
    ProcessRunDataClient processRunDataClient

    ProcessRun run(ProcessRun processRun) {
        processRunDataClient.create processRun
        assert false : 'not implemented yet'
    }
}
