package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.HtmlService
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
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
@RequestMapping('/html-doc-loader')
@Slf4j
class HtmlDocLoader {

    @Autowired
    HtmlService htmlService

    @Autowired
    DocRepository docRepository

    @Autowired
    ModuleRepository moduleRepository

    @Autowired
    ModuleMethodRepository moduleMethodRepository

    @Autowired
    ModuleMethodRunRepository moduleMethodRunRepository

    @RequestMapping(value = '/doc/{id}/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void loadDoc(@PathVariable('id') String docId) {
        try {
            ModuleMethodRun moduleMethodRun = new ModuleMethodRun(startTime: LocalDateTime.now(), parameters: [ 'docId': docId ])

            Doc doc = docRepository.findOne new ObjectId(docId)

            Document document = htmlService.load doc.uri
            doc.html = document.html()
            doc.type = 'text/html'
            doc.loadTime = LocalDateTime.now()

            docRepository.save doc

            moduleMethodRun.endTime = LocalDateTime.now()

            Module module = moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.HtmlDocLoader')
            moduleMethodRun.moduleMethodId = moduleMethodRepository.findByName('loadDoc').find({ it.moduleId == module.id }).id
            moduleMethodRunRepository.save moduleMethodRun
        } catch (e) {
            log.error 'failed', e
        }
    }
}
