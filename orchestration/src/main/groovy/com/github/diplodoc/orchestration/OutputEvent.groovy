package com.github.diplodoc.orchestration

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
class OutputEvent implements OrchestrationEvent {

    ProcessRun source
    Map<String, Object> parameters

    OutputEvent(ProcessRun source, Map<String, Object> parameters) {
        this.source = source
        this.parameters = parameters
    }

    @Override
    Collection<ProcessRun> shouldNotifyRuns(OldOrchestratorImpl orchestrator) {
        Process outputProcess = orchestrator.getProcess(source.processId)
        orchestrator.getProcessesListeningTo(outputProcess).collect { Process process ->
            ProcessRun processRun = new ProcessRun()
            processRun.processId = process.id
            processRun.parameters = parameters.collect { String key, Object value ->
                new ProcessRunParameter(key: key, value: JsonOutput.toJson(value), type: value.class.name)
            }

            return processRun
        }
    }
}
