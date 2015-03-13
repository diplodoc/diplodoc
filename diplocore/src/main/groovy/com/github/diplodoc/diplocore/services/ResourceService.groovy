package com.github.diplodoc.diplocore.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class ResourceService {

    @Autowired
    ResourceLoader resourceLoader

    void writeToFile(String directory, String name, String data) {
        resourceLoader.getResource("file://${directory}\\").createRelative(name).file.withWriter('UTF-8') {
            it.write(data)
        }
    }
}
