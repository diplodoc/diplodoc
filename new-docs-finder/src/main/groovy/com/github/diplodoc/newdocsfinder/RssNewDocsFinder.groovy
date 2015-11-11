package com.github.diplodoc.newdocsfinder

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.mongodb.data.Source
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import com.github.diplodoc.services.AuditService
import com.github.diplodoc.services.RssService
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
    @ResponseBody def newDocs(@PathVariable('id') String sourceId) {
        auditService.runMethodUnderAudit('RssNewDocsFinder', 'newDocs') { module, moduleMethod, moduleMethodRun ->
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
