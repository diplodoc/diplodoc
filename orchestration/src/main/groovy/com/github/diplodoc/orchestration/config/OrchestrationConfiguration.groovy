package com.github.diplodoc.orchestration.config

import com.github.diplodoc.domain.config.DomainConfiguration
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import com.github.diplodoc.orchestration.OldOrchestratorImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(DomainConfiguration)
class OrchestrationConfiguration {

    @Bean
    ThreadPoolTaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler()
        scheduler.poolSize = 5

        return scheduler
    }

    @Bean
    @Autowired
    OldOrchestratorImpl orchestrator(ThreadPoolTaskScheduler scheduler, ProcessRepository processRepository, ProcessRunRepository processRunRepository) {
        OldOrchestratorImpl orchestrator = new OldOrchestratorImpl()
        orchestrator.scheduler = scheduler
        orchestrator.processRepository = processRepository
        orchestrator.processRunRepository = processRunRepository

        return orchestrator
    }
}
