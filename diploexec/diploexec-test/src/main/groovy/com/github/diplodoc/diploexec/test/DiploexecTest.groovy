package com.github.diplodoc.diploexec.test

import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRepository
import org.springframework.context.ApplicationContext

/**
 * @author yaroslav.yermilov
 */
class DiploexecTest {

    ProcessRepository processRepository
    ApplicationContext modulesContext

    TestResults test(Process process) {
        new ProcessTest(process, this).test()
    }

    Process getProcess(String name) {
        processRepository.findOneByName(name)
    }
}
