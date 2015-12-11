package com.github.diplodoc.clientapi

import com.github.diplodoc.domain.DomainApplication
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

/**
 * @author yaroslav.yermilov
 */
@Import(DomainApplication)
@SpringBootApplication
class ClientApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.sources(ClientApiApplication)
    }

    static void main(String[] args) {
        SpringApplication.run(ClientApiApplication, args)
    }


    @Bean
    HttpTransport httpTransport() {
        new NetHttpTransport()
    }

    @Bean
    JsonFactory jsonFactory() {
        JacksonFactory.getDefaultInstance()
    }
}
