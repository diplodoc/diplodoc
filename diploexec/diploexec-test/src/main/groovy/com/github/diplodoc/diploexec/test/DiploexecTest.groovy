package com.github.diplodoc.diploexec.test

import com.github.diplodoc.diplobase.client.diploexec.ProcessDataClient
import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class DiploexecTest {

    @Autowired
    ProcessDataClient processDataClient

    TestResults test(Process process) {
        new ProcessTest(process, this).test()
    }

    Process getProcess(String name) {
        processDataClient.findOneByName(name)
    }
}
