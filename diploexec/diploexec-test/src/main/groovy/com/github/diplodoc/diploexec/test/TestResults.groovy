package com.github.diplodoc.diploexec.test

/**
 * @author yaroslav.yermilov
 */
class TestResults {

    String message

    static TestResults wrongTestDefinition(Throwable e) {
        new TestResults(message: 'ERROR'.padRight(15) + "in test definition: $e")
    }

    static TestResults runFailed(Throwable e) {
        new TestResults(message: 'EXCEPTION'.padRight(15) + "during test run: $e")
    }

    static TestResults verificationFailed(Throwable e) {
        new TestResults(message: 'FAILED'.padRight(15) + "test results verification: $e")
    }

    static TestResults passed() {
        new TestResults(message: 'PASSED')
    }

    @Override
    public String toString() {
        message
    }
}
