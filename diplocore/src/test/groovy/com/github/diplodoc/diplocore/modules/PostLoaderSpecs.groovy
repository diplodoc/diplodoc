package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.Web
import org.jsoup.nodes.Document
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostLoaderSpecs extends Specification {

    Web web = Mock(Web)
    PostRepository postRepository = Mock(PostRepository)
    PostLoader postLoader = new PostLoader(web: web, postRepository: postRepository)

    def 'load post'() {
        given:
            Source source = new Source()
            Document document = Mock(Document)

            Post post = new Post(url: 'url', source: source)

        when:
            document.html() >> 'html'
            web.load('url') >> document

            postRepository.save(_) >> { Post arg ->
                arg.id = 1
                return arg
            }

        then:
            Post actual = postLoader.loadPost(post)

        expect:
            actual.html == 'html'
            actual.title == null
            actual.id == 1
            actual.meaningText == null
            actual.source == source
            actual.type == null
            actual.url == 'url'
    }
}
