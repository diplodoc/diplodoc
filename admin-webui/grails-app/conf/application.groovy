grails {
    profile = 'web'
    codegen {
        defaultPackage = 'com.github.dipodoc.webui.admin'
    }
}

spring.groovy.template.'check-template-location' = false

grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [
        all:           '*/*',
        atom:          'application/atom+xml',
        css:           'text/css',
        csv:           'text/csv',
        form:          'application/x-www-form-urlencoded',
        html:          ['text/html','application/xhtml+xml'],
        js:            'text/javascript',
        json:          ['application/json', 'text/json'],
        multipartForm: 'multipart/form-data',
        rss:           'application/rss+xml',
        text:          'text/plain',
        hal:           ['application/hal+json','application/hal+xml'],
        xml:           ['text/xml', 'application/xml']
]

grails.urlmapping.cache.maxsize = 1000
grails.controllers.defaultScope = 'singleton'
grails.converters.encoding = 'UTF-8'

grails {
    views {
        'default' {
            codec = 'html'
        }
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml'
            codecs {
                expression = 'html'
                scriptlets = 'html'
                taglib = 'none'
                staticparts = 'none'
            }
        }
    }
}

if (System.getProperty('mongodb_user') != null && System.getProperty('mongodb_password') != null) {
    grails {
        mongodb {
            databaseName = 'diplodata'
            host = System.getProperty('mongodb_host') ?: 'localhost'
            port = System.getProperty('mongodb_port') ?: 27017
            username = System.getProperty('mongodb_user')
            password = System.getProperty('mongodb_password')
            connectionString = "mongodb://$username:$password@$host:$port/$databaseName"
        }
    }
} else {
    grails {
        mongodb {
            databaseName = 'diplodata'
            host = System.getProperty('mongodb_host') ?: 'localhost'
            port = System.getProperty('mongodb_port') ?: 27017
            connectionString = "mongodb://$host:$port/$databaseName"
        }
    }
}
