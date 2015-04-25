package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.VkService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/knu/vk-posts-loader')
@Slf4j
class VkPostsLoader {

    static String SEARCH_QUERY = 'кну шевченка'

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @Autowired
    VkService vkService

    @RequestMapping(value = '/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def load() {
        auditService.runMethodUnderAudit('knu.VkPostsLoader', 'load') { module, moduleMethod, moduleMethodRun ->
            Collection<Doc> docs = vkService.search(SEARCH_QUERY).collect { post ->
                new Doc(
                    uri: "vk://post/${post.owner_id}/${post.from_id}/${post.id}",
                    type: 'social/vk',
                    loadTime: LocalDateTime.now(),
                    publishTime: LocalDateTime.ofInstant(Instant.ofEpochSecond(post.date), ZoneId.systemDefault()),
                    meaningText: post.text,
                    knu: 'social'
                )
            }

            docs = docRepository.save(docs.findAll({ !docRepository.findOneByUri(it.uri) }))

            def metrics = [ 'new posts count': docs.size() ]

            [ 'metrics': metrics ]
        }
    }
}
