package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import groovy.util.logging.Slf4j

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class RunnableProcess implements Runnable {

    ProcessRun processRun
    Process process
    Map parameters

    ProcessRunManager processRunManager

    GroovyBindings groovyBindings


    @Override
    void run() {
        try {
            processRun = processRunManager.markJustStarted(processRun)
            execute()
            processRun = processRunManager.markJustSucceed(processRun)
        } catch (e) {
            processRun = processRunManager.markJustFailed(processRun, e)
        }
    }

    private execute() {
        new GroovyShell(groovyBindings.executionBinding(parameters)).evaluate(process.definition)
    }
}
