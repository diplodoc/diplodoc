package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
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
        binding.loadPost = { Map params -> loadPost(params.from, params.url) }
    }

    Post loadPost(Source source, String url) {
        log.info('loading post from {}...', url)

        Document document = web.load(url)
        Post post = new Post(url: url, html: document.html(), source: source, loadTime: LocalDateTime.now().toString())

        post = postRepository.save post
        log.debug('load post from {}', url)
        return post
    }
}
