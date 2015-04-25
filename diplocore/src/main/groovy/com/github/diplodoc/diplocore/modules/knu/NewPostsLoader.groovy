package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.HtmlService
import com.github.diplodoc.diplocore.services.RssService
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/knu/new-posts-loader')
@Slf4j
class NewPostsLoader {

    static String KNU_RSS_HOME = 'http://www.univ.kiev.ua/rss'

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @Autowired
    RssService rssService

    @Autowired
    HtmlService htmlService

    @RequestMapping(value = '/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def load() {
        auditService.runMethodUnderAudit('knu.NewPostsLoader', 'load') { module, moduleMethod, moduleMethodRun ->
            Collection<Doc> docs = rssService
                                    .feed(KNU_RSS_HOME)
                                    .findAll { rssEntry -> !docRepository.findOneByUri(rssEntry.link) }
                                    .collect { rssEntry ->
                                        new Doc(    uri: rssEntry.link,
                                                    title: rssEntry.title,
                                                    description: rssEntry.description.value,
                                                    publishTime: LocalDateTime.ofInstant(rssEntry.publishedDate.toInstant(), ZoneId.systemDefault()),
                                                    knu: 'post'
                                        )
                                    }
                                    .collect { doc ->
                                        Document document = htmlService.load doc.uri
                                        doc.html = document.html()
                                        doc.binary = doc.html.bytes
                                        doc.type = 'text/html'
                                        doc.loadTime = LocalDateTime.now()

                                        return doc
                                    }

            docs = docRepository.save docs

            def metrics = [ 'new posts count': docs.size() ]

            [ 'metrics': metrics ]
        }
    }
}
