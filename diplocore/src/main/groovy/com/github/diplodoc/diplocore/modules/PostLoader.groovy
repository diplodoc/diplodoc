package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Web
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class PostLoader implements Module {

    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.loadPost = {
            Map params -> loadPost(params.from, params.url)
        }
    }

    def loadPost(Source source, String url) {
        def document = web.load(url)
        def post = new Post(url: url, html: document.html(), source: source)

        postRepository.save post
    }
}
