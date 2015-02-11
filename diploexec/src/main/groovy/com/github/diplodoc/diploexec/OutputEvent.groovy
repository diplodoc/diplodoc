package com.github.diplodoc.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRunParameter
import groovy.json.JsonOutput
import groovy.transform.ToString

/**
 * @author yaroslav.yermilov
 */
@ToString
class OutputEvent implements DiploexecEvent {

    ProcessRun source
    Map<String, Object> parameters

    OutputEvent(ProcessRun source, Map<String, Object> parameters) {
        this.source = source
        this.parameters = parameters
    }

    @Override
    Collection<ProcessRun> notifiedRuns(Diploexec diploexec) {
        diploexec.getInputProcesses(source.process).collect { Process process ->
            ProcessRun processRun = new ProcessRun()
            processRun.process = process
            processRun.parameters = parameters.collect { String key, Object value ->
                new ProcessRunParameter(key: key, value: JsonOutput.toJson(value), type: value.class.name, processRun: processRun)
            }

            return processRun
        }
    }
}
