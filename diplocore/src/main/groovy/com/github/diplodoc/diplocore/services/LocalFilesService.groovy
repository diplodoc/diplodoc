package com.github.diplodoc.diplocore.services

import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class LocalFilesService {

    Collection<File> listFiles(String path) {
        FileUtils.listFiles(new File(path), null, true)
    }

    byte[] read(File file) {
        FileUtils.readFileToByteArray(file)
    }
}
