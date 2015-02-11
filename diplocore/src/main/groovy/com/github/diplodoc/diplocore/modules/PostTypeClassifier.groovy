package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('post-type-classifier')
@Slf4j
class PostTypeClassifier implements Bindable {

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.classify = { Post post -> classify(post) }
    }

    Post classify(Post post) {
        log.info('going to classify post from {}...', post.url)

        post = postRepository.findOne(post.id)

        if (post.meaningText.length() > 6000) {
            post.type = 'ARTICLE'
        } else {
            post.type = 'NEWS'
        }

        post = postRepository.save post
        log.debug('post {} classified as {}', post.url, post.type)
        return post
    }
}
