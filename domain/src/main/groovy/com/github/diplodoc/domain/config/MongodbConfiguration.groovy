package com.github.diplodoc.domain.config

import com.mongodb.Mongo
import org.springframework.context.annotation.Configuration
import org.springframework.data.authentication.UserCredentials
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableMongoRepositories('com.github.diplodoc.domain.repository.mongodb')
class MongodbConfiguration extends AbstractMongoConfiguration {

    @Override
    String getDatabaseName() {
        'diplodata'
    }

    @Override
    Mongo mongo() throws Exception {
        String host = System.getProperty('mongodb_host')
        String port = System.getProperty('mongodb_port')

        if (!host) {
            return new Mongo()
        }

        if (host && !port) {
            return new Mongo(host)
        }

        if (host && port) {
            return new Mongo(host, Integer.parseInt(port))
        }
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
}