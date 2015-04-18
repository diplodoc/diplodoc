package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.LocalFilesService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/local-file-loader')
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

            Collection<Doc> docs = localFilesService.listFiles(path).collect { File file ->
                new Doc(
                    knu: true,
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
