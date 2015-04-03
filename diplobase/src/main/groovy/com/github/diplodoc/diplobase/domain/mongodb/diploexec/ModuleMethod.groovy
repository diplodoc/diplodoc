package com.github.diplodoc.diplobase.domain.mongodb.diploexec

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class ModuleMethod {

    @Id
    String id


    String name

    @DBRef
    Module module
}
