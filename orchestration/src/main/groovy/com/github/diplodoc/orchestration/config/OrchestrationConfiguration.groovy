package com.github.diplodoc.orchestration.config

import com.github.diplodoc.domain.config.DomainConfiguration
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import com.github.diplodoc.orchestration.Orchestrator
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
@Import(DomainConfiguration)
class OrchestrationConfiguration {

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
    Orchestrator orchestrator(ThreadPoolTaskExecutor threadPool, ProcessRepository processRepository, ProcessRunRepository processRunRepository) {
        Orchestrator orchestrator = new Orchestrator()
        orchestrator.threadPool = threadPool
        orchestrator.processRepository = processRepository
        orchestrator.processRunRepository = processRunRepository

        return orchestrator
    }
}
