package com.github.diplodoc.newdocsfinder.config

import com.github.diplodoc.services.config.ServicesConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(ServicesConfiguration)
@ComponentScan('com.github.diplodoc.newdocsfinder')
class NewDocsFinderConfiguration {}
