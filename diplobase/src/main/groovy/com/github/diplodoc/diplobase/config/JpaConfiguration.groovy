package com.github.diplodoc.diplobase.config

import org.apache.commons.dbcp.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

/**
 * @author yaroslav.yermilov
 */
@Configuration
@EnableJpaRepositories('com.github.diplodoc.diplobase.repository.jpa')
@EnableTransactionManagement
class JpaConfiguration {

    @Bean
    DataSource dataSource() {
        DataSource dataSource = new BasicDataSource()
        dataSource.driverClassName = 'org.postgresql.Driver'
        dataSource.url = 'jdbc:postgresql://localhost:5432/diplobase'
        dataSource.username = 'postgres'
        dataSource.password = '25011992'

        return dataSource
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean()
        entityManagerFactory.dataSource = dataSource()
        entityManagerFactory.jpaVendorAdapter = jpaVendorAdapter()
        entityManagerFactory.packagesToScan = [ 'com.github.diplodoc.diplobase.domain.jpa.diploexec' ]

        return entityManagerFactory
    }

    @Bean
    @Autowired
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        new JpaTransactionManager(entityManagerFactory)
    }

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        new HibernateJpaVendorAdapter()
    }
}
