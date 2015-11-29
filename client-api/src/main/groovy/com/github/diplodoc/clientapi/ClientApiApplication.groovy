package com.github.diplodoc.clientapi

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
class ClientApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.sources(ClientApiApplication.class)
    }

    static void main(String[] args) {
        SpringApplication.run(ClientApiApplication.class, args)
    }
}
