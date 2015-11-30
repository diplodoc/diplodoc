package com.github.diplodoc.newdocsfinder.controllers

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.newdocsfinder.services.HtmlService
import groovy.util.logging.Slf4j
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * @author yaroslav.yermilov
 */
@RestController
@Slf4j
class HtmlDocLoader {

    @Autowired
    HtmlService htmlService

    @Autowired
    DocRepository docRepository

    @RequestMapping(value = '/doc/{id}/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void loadDoc(@PathVariable('id') String docId) {
        log.info "Loading document with id [$docId]..."
        Doc doc = docRepository.findOne docId

        log.info "Loading document with id [$docId] from url [$doc.uri]..."
        Document document = htmlService.load doc.uri
        doc.html = document.html()

        docRepository.save doc
        log.info "Successfully load document with id [$docId] from url [$doc.uri]"
    }
}
