package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.WwwService
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
@RequestMapping('/post-loader')
@Slf4j
class PostLoader {

    @Autowired
    WwwService wwwService

    @Autowired
    PostRepository postRepository

    @RequestMapping(value = '/post/{id}/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void loadPost(@PathVariable('id') String postId) {
        Post post = postRepository.findOne postId

        log.info('loading post from {}...', post.url)

        Document document = wwwService.load post.url
        post.html = document.html()
        post.loadTime = LocalDateTime.now()

        postRepository.save post
    }
}
