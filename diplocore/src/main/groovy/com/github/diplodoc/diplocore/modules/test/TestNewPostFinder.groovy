package com.github.diplodoc.diplocore.modules.test

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplocore.modules.Bindable
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('test.post-finder')
class TestNewPostFinder implements Bindable {

    @Override
    void bindSelf(Binding binding) {
        binding.findNewPosts = { Map params -> findNewPosts(params.action) }
    }

    void findNewPosts(Closure action) {
        [ new Post(id: '0'), new Post(id: '1') ].each { action.call(it) }
    }
}
