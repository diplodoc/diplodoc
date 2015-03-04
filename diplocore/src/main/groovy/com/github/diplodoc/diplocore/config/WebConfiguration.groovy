package com.github.diplodoc.diplocore.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableWebMvc
@ComponentScan('com.github.diplodoc.diplocore')
class WebConfiguration { }
