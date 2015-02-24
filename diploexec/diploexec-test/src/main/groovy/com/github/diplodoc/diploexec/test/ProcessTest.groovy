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

    ProcessTest(Process process, DiploexecTest diploexecTest) {
        this.process = process
        this.diploexecTest = diploexecTest
    }

    TestResults test() {
        try {
            new GroovyShell(binding()).evaluate(process.definition)
        } catch (e) {
            assert null : "not implemented yet: $e"
        }

        assert null : 'not implemented yet'
    }

    private Binding binding() {
        Binding binding = new Binding()

        binding.test = { String processUnderTestName ->
            processUnderTest = diploexecTest.getProcess(processUnderTestName)

            println "Process under test ${processUnderTest.name}"
        }

        binding.mock = { Map param ->
            mockedModules[param.module] = param.by

            println "Module ${param.module} will be mocked by ${param.by}"
        }

        return binding
        assert null : 'not implemented yet'
    }
}
