package com.github.diplodoc.orchestration.config

import com.github.diplodoc.domain.config.DomainConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(DomainConfiguration)
@ComponentScan('com.github.diplodoc.orchestration.impl')
class OrchestrationConfiguration {

    @Bean
    ThreadPoolTaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler()
        scheduler.poolSize = 5

        return scheduler
    }

    @Bean
    RestTemplate restTemplate() {
        new RestTemplate()
    }
}
