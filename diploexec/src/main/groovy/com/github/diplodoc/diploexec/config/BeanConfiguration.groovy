package com.github.diplodoc.diploexec.config

import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource

/**
 * @author yaroslav.yermilov
 */
@Configuration
@ImportResource(value = 'classpath:diploexec.context', reader = GroovyBeanDefinitionReader)
class BeanConfiguration {
}
