package com.github.diplodoc.modules.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableWebMvc
@ComponentScan('com.github.diplodoc.modules')
class ModulesWebConfiguration {}
