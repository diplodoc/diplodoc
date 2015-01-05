package com.github.diplodoc.diplobase.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration

import javax.sql.DataSource

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(RepositoryRestMvcConfiguration)
class WebConfiguration {
}
