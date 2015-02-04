package com.github.diplodoc.diplobase.client.config

import com.github.diplodoc.diplobase.config.DiplobaseConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Configuration
@ComponentScan('com.github.diplodoc.diplobase.client')
@Import(DiplobaseConfiguration)
class DiplobaseClientConfiguration {
}
