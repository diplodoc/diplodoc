package com.github.diplodoc.orchestration.config

import com.github.diplodoc.domain.config.DomainConfiguration
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import com.github.diplodoc.orchestration.GroovyBindings
import com.github.diplodoc.orchestration.ProcessInteractor
import com.github.diplodoc.orchestration.ProcessRunManager
import com.github.diplodoc.orchestration.ProcessRunner
import com.github.diplodoc.orchestration.impl.GroovyBindingsImpl
import com.github.diplodoc.orchestration.impl.LocalThreadsProcessRunner
import com.github.diplodoc.orchestration.impl.ProcessInteractorImpl
import com.github.diplodoc.orchestration.impl.ProcessRunManagerImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(DomainConfiguration)
class OrchestrationConfiguration {

    @Bean
    @Autowired
    ProcessRunner processRunner(ThreadPoolTaskScheduler scheduler, ProcessInteractor processInteractor, ProcessRunManager processRunManager, GroovyBindings groovyBindings) {
        LocalThreadsProcessRunner processRunner = new LocalThreadsProcessRunner()
        processRunner.scheduler = scheduler
        processRunner.processInteractor = processInteractor
        processRunner.processRunManager = processRunManager
        processRunner.groovyBindings = groovyBindings

        return processRunner
    }

    @Bean
    ThreadPoolTaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler()
        scheduler.poolSize = 5

        return scheduler
    }

    @Bean
    @Autowired
    ProcessInteractor processInteractor(ProcessRunner processRunner, ProcessRepository processRepository, GroovyBindings groovyBindings) {
        ProcessInteractorImpl processInteractor = new ProcessInteractorImpl()
        processInteractor.processRunner = processRunner
        processInteractor.processRepository = processRepository
        processInteractor.groovyBindings = groovyBindings

        return processInteractor
    }

    @Bean
    @Autowired
    ProcessRunManager processRunManager(ProcessRunRepository processRunRepository) {
        ProcessRunManagerImpl processRunManager = new ProcessRunManagerImpl()
        processRunManager.processRunRepository = processRunRepository

        return processRunManager
    }

    @Bean
    @Autowired
    GroovyBindings groovyBindings(RestTemplate restTemplate, ProcessInteractor processInteractor) {
        GroovyBindingsImpl groovyBindings = new GroovyBindingsImpl()
        groovyBindings.restTemplate = restTemplate
        groovyBindings.processInteractor = processInteractor

        return groovyBindings
    }

    @Bean
    RestTemplate restTemplate() {
        new RestTemplate()
    }
}
