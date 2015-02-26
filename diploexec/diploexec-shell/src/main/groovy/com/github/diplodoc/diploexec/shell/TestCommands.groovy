package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.client.diploexec.ProcessDataClient
import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diploexec.test.DiploexecTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class TestCommands implements CommandMarker {

    @Autowired
    ProcessDataClient processDataClient

    @Autowired
    DiploexecTest diploexecTest

    @CliCommand(value = 'process test', help = 'run tests for process')
    String run(@CliOption(key = '', mandatory = true, help = 'process name') final String name) {
        Process process = processDataClient.byName("test-${name}")

        diploexecTest.test(process).toString()
    }

    @CliCommand(value = 'process test-all', help = 'run tests for all processes')
    String runAll() {
        List<Process> testProcesses = processDataClient.tests()

        testProcesses.collect { Process process ->
            process.name.padRight(50) + diploexecTest.test(process)
        }.join('\n')
    }
}
