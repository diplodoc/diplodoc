package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('post-type-classifier')
class PostTypeClassifier implements Bindable {

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.classify = { Post post -> classify(post) }
    }

    Post classify(Post post) {
        post = postRepository.findOne(post.id)

        if (post.meaningText.length() > 6000) {
            post.type = 'ARTICLE'
        } else {
            post.type = 'NEWS'
        }

        postRepository.save post
    }
}
