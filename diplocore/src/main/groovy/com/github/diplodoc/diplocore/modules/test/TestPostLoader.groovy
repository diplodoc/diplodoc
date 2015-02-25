package com.github.diplodoc.diplocore.modules.test

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplocore.modules.Bindable
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('test.post-loader')
class TestPostLoader implements Bindable {

    @Override
    void bindSelf(Binding binding) {
        binding.loadPost = { Map params -> loadPost(params.post) }
    }

    Post loadPost(Post post) {
        post.html = 'html'
        return post
    }
}
