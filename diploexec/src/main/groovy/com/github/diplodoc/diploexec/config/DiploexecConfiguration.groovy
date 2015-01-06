package com.github.diplodoc.diploexec.config

import com.github.diplodoc.diplobase.config.DiplobaseConfiguration
import com.github.diplodoc.diploexec.DiploexecRuntimeEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericGroovyApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(DiplobaseConfiguration)
@ComponentScan('com.github.diplodoc.diploexec')
class DiploexecConfiguration {

    @Bean
    ThreadPoolTaskExecutor threadPool() {
        new ThreadPoolTaskExecutor()
    }

    @Bean
    ApplicationContext modulesContext() {
        new GenericGroovyApplicationContext('modules.context')
    }

    @Bean
    @Autowired
    DiploexecRuntimeEnvironment diploexecRuntimeEnvironment(ApplicationContext modulesContext, ThreadPoolTaskExecutor threadPool) {
        DiploexecRuntimeEnvironment diploexecRuntimeEnvironment = new DiploexecRuntimeEnvironment()
        diploexecRuntimeEnvironment.modulesContext = modulesContext
        diploexecRuntimeEnvironment.threadPool = threadPool

        return diploexecRuntimeEnvironment
    }
}
