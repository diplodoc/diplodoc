package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Post
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.HtmlService
import org.jsoup.nodes.Document
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostLoaderSpec extends Specification {

    PostRepository postRepository = Mock(PostRepository)
    HtmlService htmlService = Mock(HtmlService)

    PostLoader postLoader = new PostLoader(postRepository: postRepository, htmlService: htmlService)

    def 'void loadPost(String postId)'() {
        when:
            Document document = Mock(Document)
            document.html() >> 'html'

            postRepository.findOne('id') >> new Post(id: 'id', url: 'url')
            htmlService.load('url') >> document

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
