package com.github.diplodoc.diploexec.test

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process

/**
 * @author yaroslav.yermilov
 */
class ProcessTest {

    Process process
    DiploexecTest diploexecTest

    Process processUnderTest

    ProcessTest(Process process, DiploexecTest diploexecTest) {
        this.process = process
        this.diploexecTest = diploexecTest
    }

    TestResults test() {
        try {
            new GroovyShell(binding()).evaluate(process.definition)
        } catch (e) {
            assert null : 'not implemented yet'
        }

        assert null : 'not implemented yet'
    }

    private Binding binding() {
        Binding binding = new Binding()

        binding.test = { String processUnderTestName ->
            processUnderTest = diploexecTest.getProcess(processUnderTestName)
        }

        return binding
        assert null : 'not implemented yet'
    }
}
