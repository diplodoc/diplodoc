package com.github.diplodoc.diplocore.services

import org.apache.tika.config.TikaConfig
import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.sax.BodyContentHandler
import org.apache.tika.sax.ToXMLContentHandler
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

    String extractText(byte[] rawData) {
        AutoDetectParser autoDetectParser = new AutoDetectParser()
        BodyContentHandler bodyContentHandler = new BodyContentHandler()
        Metadata metadata = new Metadata()
        autoDetectParser.parse(new ByteArrayInputStream(rawData), bodyContentHandler, metadata)

        bodyContentHandler.toString()
    }

    String extractHtml(byte[] rawData) {
        AutoDetectParser autoDetectParser = new AutoDetectParser()
        ToXMLContentHandler toXMLContentHandler = new ToXMLContentHandler()
        Metadata metadata = new Metadata()
        autoDetectParser.parse(new ByteArrayInputStream(rawData), toXMLContentHandler, metadata)

        toXMLContentHandler.toString()
    }
}
