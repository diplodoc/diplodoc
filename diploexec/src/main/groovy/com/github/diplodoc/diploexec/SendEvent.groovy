package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRunParameter
import groovy.json.JsonOutput
import groovy.transform.ToString

/**
 * @author yaroslav.yermilov
 */
@ToString
class SendEvent implements DiploexecEvent {

    String destination
    Map<String, Object> parameters

    SendEvent(String destination, Map<String, Object> parameters) {
        this.destination = destination
        this.parameters = parameters
    }

    @Override
    Collection<ProcessRun> notifiedRuns(Diploexec diploexec) {
        ProcessRun processRun = new ProcessRun()
        processRun.process = diploexec.getProcess(destination)
        processRun.parameters = parameters.collect { String key, Object value ->
            new ProcessRunParameter(key: key, value: JsonOutput.toJson(value), type: value.class.name, processRun: processRun)
        }

        [ processRun ]
    }
}
