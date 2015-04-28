package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import groovy.util.logging.Slf4j
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
@RequestMapping('/knu/socials-sentiment-analyzer')
@Slf4j
class SocialsSentimentAnalyzer {

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/train-model', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def trainModel() {
        auditService.runMethodUnderAudit('knu.SocialsSentimentAnalyzer', 'trainModel') { module, moduleMethod, moduleMethodRun ->
            assert null: 'not implemented yet'
        }
    }

    @RequestMapping(value = '/doc/{id}/analyze-sentiment', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def analyzeSentiment(@PathVariable('id') String docId) {
        auditService.runMethodUnderAudit('knu.SocialsSentimentAnalyzer', 'analyzeSentiment') { module, moduleMethod, moduleMethodRun ->
            moduleMethodRun.parameters = [ 'docId': docId ]

            assert null: 'not implemented yet'

            [ 'moduleMethodRun': moduleMethodRun ]
        }
    }
}
