package com.github.diplodoc.diplocore.config

import com.github.diplodoc.diplobase.config.DiplobaseConfiguration
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(DiplobaseConfiguration)
@ComponentScan('com.github.diplodoc.diplocore')
class DiplocoreConfiguration {
}
