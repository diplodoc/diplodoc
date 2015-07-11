package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun

/**
 * @author yaroslav.yermilov
 */
interface OrchestrationEvent {

    Collection<ProcessRun> shouldNotifyRuns(OldOrchestratorImpl orchestrator)
}
