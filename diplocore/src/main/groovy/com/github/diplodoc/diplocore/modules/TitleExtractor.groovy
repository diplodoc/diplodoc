package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Web
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('title-extractor')
@Slf4j
class TitleExtractor implements Bindable {

    @Autowired
    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.extractTitle = { Map params -> extractTitle(params.from) }
    }

    Post extractTitle(Post post) {
        log.info('going to extract title from {}...', post.url)
        post = postRepository.findOne(post.id)
        post.title = web.document(post).select('meta[property=og:title]').attr('content')
        post = postRepository.save post
        log.debug('for post {} title extracted: {}', post.url, post.title)
        return post
    }
}
