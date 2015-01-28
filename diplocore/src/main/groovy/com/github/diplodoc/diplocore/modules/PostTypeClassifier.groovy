package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('post-type-classifier')
class PostTypeClassifier implements Bindable {

    @Override
    void bindSelf(Binding binding) {
        binding.classify = { Post post -> classify(post) }
    }

    Post classify(Post post) {
        assert false : 'not implemented yet'
    }
}
