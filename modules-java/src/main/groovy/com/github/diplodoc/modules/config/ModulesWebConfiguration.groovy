package com.github.diplodoc.modules.config

import com.github.diplodoc.domain.config.DomainConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableWebMvc
@Import(DomainConfiguration)
@ComponentScan('com.github.diplodoc.modules')
class ModulesWebConfiguration {}
