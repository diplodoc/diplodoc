package com.github.diplodoc.diplobase.config

import com.mongodb.Mongo
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableMongoRepositories('com.github.diplodoc.diplobase.repository.mongodb')
class MongodbConfiguration extends AbstractMongoConfiguration {

    @Override
    String getDatabaseName() {
        'diplodata'
    }

    @Override
    Mongo mongo() throws Exception {
        new Mongo()
    }

    @Override
    String getMappingBasePackage() {
        'com.github.diplodoc.diplobase.domain.mongodb'
    }
}