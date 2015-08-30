package com.github.diplodoc.domain.mongodb.orchestration

import groovy.json.JsonOutput
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * @author yaroslav.yermilov
 */
@EqualsAndHashCode
@ToString
class ProcessRunParameter {

    static ProcessRunParameter fromKeyValue(def key, def value) {
        new ProcessRunParameter(key: key, type: value.class.canonicalName, value: JsonOutput.toJson(value))
    }

    String key

    String type

    String value
}
