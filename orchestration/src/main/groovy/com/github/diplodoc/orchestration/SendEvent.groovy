package com.github.diplodoc.orchestration

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
class SendEvent implements OrchestrationEvent {

    String destination
    Map<String, Object> parameters

    SendEvent(String destination, Map<String, Object> parameters) {
        this.destination = destination
        this.parameters = parameters
    }

    @Override
    Collection<ProcessRun> shouldNotifyRuns(OldOrchestratorImpl orchestrator) {
        ProcessRun processRun = new ProcessRun()
        processRun.processId = orchestrator.getProcess(destination).id
        processRun.parameters = parameters.collect { String key, Object value ->
            new ProcessRunParameter(key: key, value: JsonOutput.toJson(value), type: value.class.name)
        }

        [ processRun ]
    }
}
