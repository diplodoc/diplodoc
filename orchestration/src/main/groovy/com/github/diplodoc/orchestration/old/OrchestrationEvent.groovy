package com.github.diplodoc.orchestration.old

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface OrchestrationEvent {

    Collection<ProcessRun> shouldNotifyRuns(OldOrchestratorImpl orchestrator)
}
