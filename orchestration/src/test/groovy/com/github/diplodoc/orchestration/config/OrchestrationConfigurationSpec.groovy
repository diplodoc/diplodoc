package com.github.diplodoc.orchestration.config

import com.github.diplodoc.orchestration.OrchestrationApplication
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class OrchestrationConfigurationSpec extends Specification {

    OrchestrationApplication orchestrationApplication = new OrchestrationApplication()

    def 'ThreadPoolTaskScheduler scheduler()'() {
        when:
            def actual = orchestrationApplication.scheduler()

        then:
            actual instanceof ThreadPoolTaskScheduler
            actual.poolSize == 5
    }

    def 'RestTemplate restTemplate()'() {
        when:
            def actual = orchestrationApplication.restTemplate()

        then:
            actual instanceof RestTemplate
    }
}
