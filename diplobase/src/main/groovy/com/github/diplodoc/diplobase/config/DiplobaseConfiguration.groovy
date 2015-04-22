package com.github.diplodoc.diplobase.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Configuration
@Import(MongodbConfiguration)
class DiplobaseConfiguration {}
