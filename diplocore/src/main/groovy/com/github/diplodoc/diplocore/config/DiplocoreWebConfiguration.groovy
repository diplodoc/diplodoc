package com.github.diplodoc.diplocore.config

import com.github.diplodoc.diplobase.config.DiplobaseConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableWebMvc
@Import(DiplobaseConfiguration)
@ComponentScan('com.github.diplodoc.diplocore.modules')
class DiplocoreWebConfiguration {}
