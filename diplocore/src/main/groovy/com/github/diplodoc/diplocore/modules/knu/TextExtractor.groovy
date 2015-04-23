package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.RawDataService
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/knu/text-extractor')
@Slf4j
class TextExtractor {

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @Autowired
    RawDataService rawDataService

    @RequestMapping(value = '/doc/{id}/text', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def extractText(@PathVariable('id') String docId) {
        auditService.runMethodUnderAudit('knu.TextExtractor', 'extractText') { module, moduleMethod, moduleMethodRun ->
            moduleMethodRun.parameters = [ 'docId': docId ]

            Doc doc = docRepository.findOne new ObjectId(docId)

            doc.meaningText = rawDataService.extractText(doc.binary)
            doc.meaningHtml = rawDataService.extractHtml(doc.binary)

            docRepository.save doc

            [ 'moduleMethodRun': moduleMethodRun ]
        }
    }
}
