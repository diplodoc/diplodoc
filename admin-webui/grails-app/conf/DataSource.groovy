environments {
    development {
        grails {
            mongo {
                host = 'localhost'
                port = 27017
                databaseName = 'diplodata'
            }
        }
    }
    production {
        grails {
            mongo {
                databaseName = 'diplodata'
                host = System.getProperty 'mongodb_host'
                port = System.getProperty 'mongodb_port'
                username = System.getProperty 'mongodb_user'
                password = System.getProperty 'mongodb_password'
            }
        }
    }
}
