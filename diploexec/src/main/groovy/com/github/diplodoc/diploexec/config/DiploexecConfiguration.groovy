package com.github.diplodoc.diploexec.config

import com.github.diplodoc.diplobase.config.DiplobaseConfiguration
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRepository
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRunRepository
import com.github.diplodoc.diplocore.config.DiplocoreConfiguration
import com.github.diplodoc.diploexec.Diploexec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Lazy
import org.springframework.context.support.GenericGroovyApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import([ DiplobaseConfiguration, DiplocoreConfiguration ])
@ComponentScan('com.github.diplodoc.diploexec')
class DiploexecConfiguration {

    @Bean
    ThreadPoolTaskExecutor threadPool() {
        new ThreadPoolTaskExecutor()
    }

    @Bean
    @Autowired
    @Lazy
    Diploexec diploexec(ThreadPoolTaskExecutor threadPool, ApplicationContext modulesContext, ProcessRepository processRepository, ProcessRunRepository processRunRepository) {
        Diploexec diploexec = new Diploexec()
        diploexec.threadPool = threadPool
        diploexec.modulesContext = modulesContext
        diploexec.processRepository = processRepository
        diploexec.processRunRepository = processRunRepository

        return diploexec
    }
}
