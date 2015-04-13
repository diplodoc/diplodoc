package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.SourceRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.RssService
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/rss-new-docs-finder')
@Slf4j
class RssNewDocsFinder {

    @Autowired
    DocRepository docRepository

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    RssService rssService

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/source/{id}/new-docs', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody Collection<String> newDocs(@PathVariable('id') String sourceId) {
        auditService.runMethodUnderAudit('com.github.diplodoc.diplocore.modules.RssNewDocsFinder', 'newDocs') { module, moduleMethod, moduleMethodRun ->
            moduleMethodRun.parameters = [ 'sourceId': sourceId ]

            Source source = sourceRepository.findOne new ObjectId(sourceId)

            Collection<Doc> docs = rssService
                                        .feed(source.rssUrl)
                                        .findAll { rssEntry -> !docRepository.findOneByUri(rssEntry.link) }
                                        .collect { rssEntry ->
                                            new Doc(    uri: rssEntry.link,
                                                        sourceId: new ObjectId(sourceId),
                                                        title: rssEntry.title,
                                                        description: rssEntry.description.value,
                                                        publishTime: LocalDateTime.ofInstant(rssEntry.publishedDate.toInstant(), ZoneId.systemDefault())
                                            )
                                        }
            docs = docRepository.save docs

            def result = docs*.id*.toString()
            def metrics = [ 'new docs count': docs.size() ]

            [ 'result': result, 'metrics': metrics, 'moduleMethodRun': moduleMethodRun ]
        }
    }
}
