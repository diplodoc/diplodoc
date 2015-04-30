package com.github.diplodoc.domain.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(MongodbConfiguration)
class DomainConfiguration {}
