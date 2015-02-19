package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.Web
import groovy.util.logging.Slf4j
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Component('post-loader')
@Slf4j
class PostLoader implements Bindable {

    @Autowired
    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.loadPost = { Map params -> loadPost(params.post) }
    }

    Post loadPost(Post post) {
        log.info('loading post from {}...', post.url)

        Document document = web.load(post.url)
        post.html = document.html()
        post.loadTime = LocalDateTime.now()

        post = postRepository.save post
        log.debug('load post {}', post)
        return post
    }
}
