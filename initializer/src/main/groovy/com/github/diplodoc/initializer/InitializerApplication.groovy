package com.github.diplodoc.initializer

import com.github.diplodoc.services.config.ServicesConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Import(ServicesConfiguration)
@SpringBootApplication
class InitializerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.sources(InitializerApplication.class)
    }

    static void main(String[] args) {
        SpringApplication.run(InitializerApplication.class, args)
    }
}
