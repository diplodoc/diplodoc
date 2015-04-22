package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface DiploexecEvent {

    Collection<ProcessRun> shouldNotifyRuns(Diploexec diploexec)
}
