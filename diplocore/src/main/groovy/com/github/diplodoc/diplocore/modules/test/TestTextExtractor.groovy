package com.github.diplodoc.diplocore.modules.test

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplocore.modules.Bindable
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('test.text-extractor')
class TestTextExtractor implements Bindable {

    @Override
    void bindSelf(Binding binding) {
        binding.extractText = { Map params -> extractText(params.from) }
    }

    Post extractText(Post post) {
        post.meaningText = 'meaning text'
        return post
    }
}
