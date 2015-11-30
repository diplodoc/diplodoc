package com.github.diplodoc.domain

import com.mongodb.Mongo
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.authentication.UserCredentials
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

/**
 * @author yaroslav.yermilov
 */
@SpringBootApplication
class DomainApplication extends AbstractMongoConfiguration {

    @Override
    String getDatabaseName() {
        'diplodata'
    }

    @Override
    Mongo mongo() throws Exception {
        String host = System.getProperty('mongodb_host')
        String port = System.getProperty('mongodb_port')

        if (host != null && port != null) {
            return new Mongo(host, Integer.parseInt(port))
        }

        if (host != null) {
            return new Mongo(host)
        }

        return new Mongo()
    }

    @Override
    String getMappingBasePackage() {
        'com.github.diplodoc.domain.mongodb'
    }

    @Override
    protected UserCredentials getUserCredentials() {
        String username = System.getProperty('mongodb_user')
        String password = System.getProperty('mongodb_password')

        if (username) {
            return new UserCredentials(username, password)
        }

        return null
    }

    static void main(String[] args) {
        SpringApplication.run(DomainApplication, args)
    }
}
