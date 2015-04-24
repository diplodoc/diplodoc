package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.FacebookService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.social.facebook.api.Post
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
@RequestMapping('/knu/facebook-posts-loader')
@Slf4j
class FacebookPostsLoader {

    static String[] KNU_GROUPS = [ 'kyiv.university', 'shevapil' ]

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @Autowired
    FacebookService facebookService

    @RequestMapping(value = '/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def load() {
        auditService.runMethodUnderAudit('knu.FacebookPostsLoader', 'load') { module, moduleMethod, moduleMethodRun ->
            Collection<Doc> docs = KNU_GROUPS.collectMany { String knuGroup ->
                facebookService.groupPosts(knuGroup).collect { Post post ->
                    new Doc(
                        uri: "facebook://post/${post.id}",
                        type: 'social/facebook',
                        loadTime: LocalDateTime.now(),
                        title: post.caption,
                        description: post.description,
                        publishTime: LocalDateTime.ofInstant(post.createdTime.toInstant(), ZoneId.systemDefault()),
                        meaningText: post.message,
                        knuSocial: true
                    )
                }
            }

            docs = docRepository.save(docs.findAll({ !docRepository.findOneByUri(it.uri) }))

            def metrics = [ 'new posts count': docs.size() ]

            [ 'metrics': metrics ]
        }
    }
}
