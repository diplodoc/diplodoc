package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.WwwService
import org.jsoup.nodes.Document
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostLoaderSpec extends Specification {

    PostRepository postRepository = Mock(PostRepository)
    WwwService wwwService = Mock(WwwService)

    PostLoader postLoader = new PostLoader(postRepository: postRepository, wwwService: wwwService)

    def 'void loadPost(String postId)'() {
        when:
            Document document = Mock(Document)
            document.html() >> 'html'

            postRepository.findOne('id') >> new Post(id: 'id', url: 'url')
            wwwService.load('url') >> document

            postLoader.loadPost('id')

        then:
            1 * postRepository.save({ Post post ->
                post.id == 'id' &&
                post.url == 'url' &&
                post.html == 'html' &&
                post.loadTime != null
            })
    }
}
