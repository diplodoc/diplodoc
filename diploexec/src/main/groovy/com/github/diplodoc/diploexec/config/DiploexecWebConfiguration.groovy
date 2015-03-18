package com.github.diplodoc.diploexec.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableWebMvc
@ComponentScan('com.github.diplodoc.diploexec.controller')
class DiploexecWebConfiguration { }
