package com.github.diplodoc.services

import org.apache.commons.lang3.SerializationUtils
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class SerializationService {

    byte[] serialize(def object) {
        SerializationUtils.serialize(object)
    }

    def deserialize(byte[] serializedObject) {
        SerializationUtils.deserialize(serializedObject)
    }
}
