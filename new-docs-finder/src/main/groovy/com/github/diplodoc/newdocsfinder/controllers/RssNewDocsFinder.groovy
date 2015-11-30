package com.github.diplodoc.newdocsfinder.controllers

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.mongodb.data.Source
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import com.github.diplodoc.newdocsfinder.services.RssService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author yaroslav.yermilov
 */
@RestController
@Slf4j
class RssNewDocsFinder {

    @Autowired
    DocRepository docRepository

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    RssService rssService

    @RequestMapping(value = '/source/{id}/new-docs/ids', method = RequestMethod.POST)
    List<String> newDocs(@PathVariable('id') String sourceId) {
        log.info "Finding new docs from source with id [$sourceId]..."

        Source source = sourceRepository.findOne sourceId
        log.info "Finding new docs from source with id [$sourceId] from rss url [$source.rssUrl]..."

        int noPublishedDateCount = 0

        Collection<Doc> docs = rssService
                                    .feed(source.rssUrl)
                                    .findAll { rssEntry -> !docRepository.findOneByUri(rssEntry.link) }
                                    .collect { rssEntry ->
                                        if (rssEntry.publishedDate == null) {
                                            noPublishedDateCount++
                                            log.warn "RSS entry [$rssEntry.link] has no published date set"
                                        }

                                        Doc doc = new Doc(
                                            uri: rssEntry.link,
                                            sourceId: sourceId,
                                            title: rssEntry.title,
                                            publishTime: toJava8DateTimeApi(rssEntry.publishedDate)
                                        )
                                        log.info "Load doc: [$doc]"

                                        return doc
                                    }

        log.info "Found ${docs.size()} new docs"
        docs = docRepository.save docs

        return docs*.id*.toString()
    }

    static LocalDateTime toJava8DateTimeApi(Date date) {
        LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
    }
}
