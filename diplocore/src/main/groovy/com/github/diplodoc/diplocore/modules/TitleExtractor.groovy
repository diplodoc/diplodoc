package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Web
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class TitleExtractor implements Module {

    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.extractTitle = {
            Map params -> extractTitle(params.from)
        }
    }

    def extractTitle(Post post) {
        post = postRepository.findOne(post.id)
        post.title = web.document(post).select('meta[property=og:title]').attr('content')
        postRepository.save post
    }
}
