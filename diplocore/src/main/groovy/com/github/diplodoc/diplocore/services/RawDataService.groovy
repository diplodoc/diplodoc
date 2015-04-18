package com.github.diplodoc.diplocore.services

import org.apache.tika.config.TikaConfig
import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class RawDataService {

    TikaConfig tikaConfig = new TikaConfig()

    String detectType(String uri, byte[] rawData) {
        Metadata metadata = new Metadata()
        metadata.set(Metadata.RESOURCE_NAME_KEY, uri)
        tikaConfig.getDetector().detect(TikaInputStream.get(rawData), metadata).toString()
    }
}
