package com.github.diplodoc.orchestration

import com.github.diplodoc.domain.DomainApplication
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Import(DomainApplication)
@SpringBootApplication
class OrchestrationApplication extends SpringBootServletInitializer {

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

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.sources(OrchestrationApplication)
    }

    static void main(String[] args) {
        SpringApplication.run(OrchestrationApplication, args)
    }
}
