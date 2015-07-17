package com.github.diplodoc.orchestration.old

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRunParameter
import groovy.json.JsonOutput
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * @author yaroslav.yermilov
 */
@ToString
@EqualsAndHashCode
class NotifyEvent implements OrchestrationEvent {

    String eventName
    Map<String, Object> parameters

    NotifyEvent(String eventName, Map<String, Object> parameters) {
        this.eventName = eventName
        this.parameters = parameters
    }

    @Override
    Collection<ProcessRun> shouldNotifyRuns(OldOrchestratorImpl orchestrator) {
        orchestrator.getProcessesWaitingFor(eventName).collect { Process process ->
            ProcessRun processRun = new ProcessRun()
            processRun.processId = process.id
            processRun.parameters = parameters.collect { String key, Object value ->
                new ProcessRunParameter(key: key, value: JsonOutput.toJson(value), type: value.class.name)
            }

            return processRun
        }
    }
}
