package com.github.diplodoc.modules.services

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SerializationServiceSpec extends Specification {

    SerializationService serializationService = new SerializationService()

    def 'serialize and deserialize'() {
        given:
            def object = 'object'

        when:
            def serialized = serializationService.serialize object
            def deserialized = serializationService.deserialize serialized

        then:
            object == deserialized
    }
}
