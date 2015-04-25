package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.TwitterService
import groovy.util.logging.Slf4j
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
@RequestMapping('/knu/twitter-posts-loader')
@Slf4j
class TwitterPostsLoader {

    static String SEARCH_QUERY = 'кну шевченка'

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @Autowired
    TwitterService twitterService

    @RequestMapping(value = '/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def load() {
        auditService.runMethodUnderAudit('knu.TwitterPostsLoader', 'load') { module, moduleMethod, moduleMethodRun ->
            Collection<Doc> docs = twitterService.search(SEARCH_QUERY).collect { tweet ->
                new Doc(
                    uri: "twitter://tweet/${tweet.id}",
                    type: 'social/twitter',
                    loadTime: LocalDateTime.now(),
                    publishTime: LocalDateTime.ofInstant(tweet.createdAt.toInstant(), ZoneId.systemDefault()),
                    meaningText: tweet.text,
                    knu: 'social'
                )
            }

            docs = docRepository.save(docs.findAll({ !docRepository.findOneByUri(it.uri) }))

            def metrics = [ 'new posts count': docs.size() ]

            [ 'metrics': metrics ]
        }
    }
}
