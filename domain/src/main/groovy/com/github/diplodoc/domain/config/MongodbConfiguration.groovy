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
        new Mongo(System.getProperty('mongodb_host'), System.getProperty('mongodb_port'))
    }

    @Override
    String getMappingBasePackage() {
        'com.github.diplodoc.domain.mongodb'
    }

    @Override
    protected UserCredentials getUserCredentials() {
        new UserCredentials(System.getProperty('mongodb_user'), System.getProperty('mongodb_password'))
    }
}