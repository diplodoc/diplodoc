package com.github.diplodoc.diploexec.config

import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ProcessRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ProcessRunRepository
import com.github.diplodoc.diplocore.config.DiplocoreConfiguration
import com.github.diplodoc.diploexec.Diploexec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(DiplocoreConfiguration)
class DiploexecConfiguration {

    @Bean
    ThreadPoolTaskExecutor threadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 5
        taskExecutor.maxPoolSize = 20

        return taskExecutor
    }

    @Bean
    @Autowired
    @Lazy
    Diploexec diploexec(ThreadPoolTaskExecutor threadPool, ProcessRepository processRepository, ProcessRunRepository processRunRepository) {
        Diploexec diploexec = new Diploexec()
        diploexec.threadPool = threadPool
        diploexec.processRepository = processRepository
        diploexec.processRunRepository = processRunRepository

        return diploexec
    }
}
