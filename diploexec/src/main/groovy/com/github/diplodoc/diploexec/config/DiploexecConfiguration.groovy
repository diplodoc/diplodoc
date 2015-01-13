package com.github.diplodoc.diploexec.config

import com.github.diplodoc.diplobase.client.ProcessDataClient
import com.github.diplodoc.diplobase.client.ProcessRunDataClient
import com.github.diplodoc.diplobase.config.DiplobaseConfiguration
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
    ProcessDataClient processDataClient() {
        new ProcessDataClient('http://localhost:8080')
    }

    @Bean
    ProcessRunDataClient processRunDataClient() {
        new ProcessRunDataClient('http://localhost:8080')
    }

    @Bean
    @Autowired
    @Lazy
    Diploexec diploexec(ApplicationContext modulesContext, ThreadPoolTaskExecutor threadPool, ProcessDataClient processDataClient, ProcessRunDataClient processRunDataClient) {
        Diploexec diploexec = new Diploexec()
        diploexec.threadPool = threadPool
        diploexec.modulesContext = modulesContext
        diploexec.processDataClient = processDataClient
        diploexec.processRunDataClient = processRunDataClient

        return diploexec
    }
}
