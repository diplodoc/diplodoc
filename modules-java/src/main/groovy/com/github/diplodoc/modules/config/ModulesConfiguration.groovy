package com.github.diplodoc.modules.config

import com.github.diplodoc.domain.config.DomainConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(DomainConfiguration)
@ComponentScan('com.github.diplodoc.modules')
class ModulesConfiguration {}
