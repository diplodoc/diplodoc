package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostTypeClassifierSpecs extends Specification {

    PostRepository postRepository = Mock(PostRepository)
    PostTypeClassifier postTypeClassifier = new PostTypeClassifier(postRepository: postRepository)

    def 'classify article'() {
        when:
            Source source = new Source()
            Post post = new Post()
            post.html = 'html'
            post.id = 1
            post.source = source
            post.url = 'url'
            post.meaningText = ('-' * 10000)

            postRepository.findOne(1) >> post

            postRepository.save(_) >> { Post arg -> arg }

        then:
            Post actual = postTypeClassifier.classify(post)

        expect:
            actual.html == 'html'
            actual.title == null
            actual.id == 1
            actual.meaningText == ('-' * 10000)
            actual.source == source
            actual.type == 'ARTICLE'
            actual.url == 'url'
    }

    def 'classify news'() {
        when:
            Source source = new Source()
            Post post = new Post()
            post.html = 'html'
            post.id = 1
            post.source = source
            post.url = 'url'
            post.meaningText = ('-' * 1000)

            postRepository.findOne(1) >> post

            postRepository.save(_) >> { Post arg -> arg }

        then:
            Post actual = postTypeClassifier.classify(post)

        expect:
            actual.html == 'html'
            actual.title == null
            actual.id == 1
            actual.meaningText == ('-' * 1000)
            actual.source == source
            actual.type == 'NEWS'
            actual.url == 'url'
    }
}
