package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun

import java.util.concurrent.Callable

/**
 * @author yaroslav.yermilov
 */
class ProcessCall implements Callable<ProcessCallResult> {

    ProcessRun processRun

    ProcessCall(ProcessRun processRun) {
        this.processRun = processRun
    }

    @Override
    ProcessCallResult call() throws Exception {
        assert false : 'not implemented yet'
    }
}
