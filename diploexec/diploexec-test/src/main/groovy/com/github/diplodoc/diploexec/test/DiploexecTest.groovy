package com.github.diplodoc.diploexec.test

import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRepository
import com.github.diplodoc.diplocore.modules.Bindable
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

    Bindable getModule(String name) {
        modulesContext.getBean(name)
    }
}
