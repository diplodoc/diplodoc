grails {
    mongo {
        host = 'localhost'
        port = 27017
        databaseName = 'diplodata'
    }
}

dataSource {
    pooled = true
    jmxExport = true

    driverClassName = 'org.postgresql.Driver'
    url = 'jdbc:postgresql://localhost:5432/diplobase'
    username = 'postgres'
    password = '25011992'

    dbCreate = 'validate'
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
    singleSession = true
    flush.mode = 'manual'
}
