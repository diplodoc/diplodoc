package com.github.diplodoc.diplobase.domain.mongodb.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode(includes = 'id')
@ToString
class Module {

    @Id
    String id


    String name

    Map data
}
