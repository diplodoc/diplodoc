package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Process
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRunParameter
import groovy.json.JsonOutput
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * @author yaroslav.yermilov
 */
@ToString
@EqualsAndHashCode
class NotifyEvent implements DiploexecEvent {

    String eventName
    Map<String, Object> parameters

    NotifyEvent(String eventName, Map<String, Object> parameters) {
        this.eventName = eventName
        this.parameters = parameters
    }

    @Override
    Collection<ProcessRun> shouldNotifyRuns(Diploexec diploexec) {
        diploexec.getProcessesWaitingFor(eventName).collect { Process process ->
            ProcessRun processRun = new ProcessRun()
            processRun.process = process
            processRun.parameters = parameters.collect { String key, Object value ->
                new ProcessRunParameter(key: key, value: JsonOutput.toJson(value), type: value.class.name, processRun: processRun)
            }

            return processRun
        }
    }
}
