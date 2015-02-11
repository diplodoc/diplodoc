package com.github.diplodoc.diplobase.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import([ JpaConfiguration, MongodbConfiguration ])
class DiplobaseConfiguration { }
