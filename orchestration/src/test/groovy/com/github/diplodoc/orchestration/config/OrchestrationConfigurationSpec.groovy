package com.github.diplodoc.orchestration.config

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class OrchestrationConfigurationSpec extends Specification {

    OrchestrationConfiguration orchestrationConfiguration = new OrchestrationConfiguration()

    def 'ThreadPoolTaskScheduler scheduler()'() {
        when:
            def actual = orchestrationConfiguration.scheduler()

        then:
            actual instanceof ThreadPoolTaskScheduler
            actual.poolSize == 5
    }

    def 'RestTemplate restTemplate()'() {
        when:
            def actual = orchestrationConfiguration.restTemplate()

        then:
            actual instanceof RestTemplate
    }
}
