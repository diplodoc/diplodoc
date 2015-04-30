package com.github.diplodoc.modules.knu

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.modules.services.AuditService
import com.github.diplodoc.modules.services.LocalFilesService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/knu/local-file-loader')
@Slf4j
class LocalFileLoader {

    @Autowired
    LocalFilesService localFilesService

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def loadFiles(@RequestParam('path') path) {
        auditService.runMethodUnderAudit('knu.LocalFileLoader', 'loadFiles') { module, moduleMethod, moduleMethodRun ->
            moduleMethodRun.parameters = [ 'path': path ]

            Collection<Doc> docs = localFilesService
                                    .listFiles(path)
                                    .findAll { File file -> !docRepository.findOneByUri("file://localhost/${file.absolutePath}") }
                                    .collect { File file ->
                                        new Doc(
                                            knu: 'document',
                                            uri: "file://localhost/${file.absolutePath}",
                                            loadTime: LocalDateTime.now(),
                                            binary: localFilesService.read(file)
                                        )
                                    }

            def metrics = [ 'new files count': docs.size() ]

            docs.each(docRepository.&save)

            [ 'metrics': metrics, 'moduleMethodRun': moduleMethodRun ]
        }
    }
}
