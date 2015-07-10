package com.github.diplodoc.orchestration.config

import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRunRepository
import com.github.diplodoc.orchestration.Orchestrator
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import spock.lang.Ignore
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class OrchestrationConfigurationSpec extends Specification {

    OrchestrationConfiguration orchestrationConfiguration = new OrchestrationConfiguration()

    @Ignore
    def 'ThreadPoolTaskExecutor threadPool()'() {
        when:
            def actual = orchestrationConfiguration.threadPool()

        then:
            actual instanceof ThreadPoolTaskExecutor
            actual.corePoolSize == 5
            actual.maxPoolSize == 20
    }

    @Ignore
    def 'Orchestrator orchestrator(ThreadPoolTaskExecutor threadPool, ProcessRepository processRepository, ProcessRunRepository processRunRepository)'() {
        given:
            ThreadPoolTaskExecutor threadPoolTaskExecutor = Mock(ThreadPoolTaskExecutor)
            ProcessRepository processRepository = Mock(ProcessRepository)
            ProcessRunRepository processRunRepository = Mock(ProcessRunRepository)

        when:
            def actual = orchestrationConfiguration.orchestrator(threadPoolTaskExecutor, processRepository, processRunRepository)

        then:
            actual instanceof Orchestrator
            actual.threadPool == threadPoolTaskExecutor
            actual.processRepository == processRepository
            actual.processRunRepository == processRunRepository
    }
}
