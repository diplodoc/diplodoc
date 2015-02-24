package com.github.diplodoc.diploexec.test

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import groovy.util.logging.Slf4j

/**
 * @author yaroslav.yermilov
 */
@Slf4j
class ProcessTest {

    Process process
    DiploexecTest diploexecTest

    Process processUnderTest
    Map<String, String> mockedModules = [:]
    Closure verifications

    ProcessTest(Process process, DiploexecTest diploexecTest) {
        this.process = process
        this.diploexecTest = diploexecTest
    }

    TestResults test() {
        new GroovyShell(testDefinitionBinding()).evaluate(process.definition)

        assert null : 'not implemented yet'
    }

    private Binding testDefinitionBinding() {
        Binding binding = new Binding()

        binding.test = { String processUnderTestName ->
            processUnderTest = diploexecTest.getProcess(processUnderTestName)
        }

        binding.mock = { Map param ->
            mockedModules[param.module] = param.by
        }

        binding.verify = { Closure verifications ->
            this.verifications = verifications
        }

        return binding
    }
}
