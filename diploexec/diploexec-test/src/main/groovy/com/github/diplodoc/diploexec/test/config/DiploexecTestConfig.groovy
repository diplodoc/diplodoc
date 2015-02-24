package com.github.diplodoc.diploexec.test.config

import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRepository
import com.github.diplodoc.diplocore.config.DiplocoreConfiguration
import com.github.diplodoc.diploexec.test.DiploexecTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author yaroslav.yermilov
 */
@Configuration
class DiploexecTestConfig {

    @Bean
    @Autowired
    @Lazy
    DiploexecTest diploexecTest(ProcessRepository processRepository) {
        DiploexecTest diploexecTest = new DiploexecTest()
        diploexecTest.modulesContext = modulesContext()
        diploexecTest.processRepository = processRepository

        return diploexecTest
    }

    ApplicationContext modulesContext() {
        new AnnotationConfigApplicationContext(DiplocoreConfiguration)
    }
}
