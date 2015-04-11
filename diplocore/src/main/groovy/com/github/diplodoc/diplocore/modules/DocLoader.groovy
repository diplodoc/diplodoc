package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.HtmlService
import groovy.util.logging.Slf4j
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/doc-loader')
@Slf4j
class DocLoader {

    @Autowired
    HtmlService htmlService

    @Autowired
    DocRepository docRepository

    @RequestMapping(value = '/doc/{id}/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void loadDoc(@PathVariable('id') String docId) {
        Doc doc = docRepository.findOne docId

        log.info('loading doc from {}...', doc.url)

        Document document = htmlService.load doc.url
        doc.html = document.html()
        doc.loadTime = LocalDateTime.now()

        docRepository.save doc
    }
}
