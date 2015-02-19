package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface DiploexecEvent {

    Collection<ProcessRun> notifiedRuns(Diploexec diploexec)
}
