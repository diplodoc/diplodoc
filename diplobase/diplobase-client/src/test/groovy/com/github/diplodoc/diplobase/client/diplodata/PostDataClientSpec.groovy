package com.github.diplodoc.diplobase.client.diplodata

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostDataClientSpec extends Specification {

    PostRepository postRepository = Mock(PostRepository)
    PostDataClient postDataClient = new PostDataClient(postRepository: postRepository)

    def 'Iterable<Post> findAllWithLimit(int limit)'() {
        when:
            def actual = postDataClient.findAllWithLimit(28)

        then:
            1 * postRepository.findAll(new PageRequest(0, 28, Sort.Direction.DESC, 'id')) >> new PageImpl<Post>([
                new Post(id: 1, loadTime: 'load-time-1', source: new Source(name: 'source-name-1'), url: 'url-1'),
                new Post(id: 2, loadTime: 'load-time-2', source: new Source(name: 'source-name-2'), url: 'url-2')
            ])

        expect:
            actual.size() == 2
            actual[0] == new Post(id: 1, loadTime: 'load-time-1', source: new Source(name: 'source-name-1'), url: 'url-1')
            actual[1] == new Post(id: 2, loadTime: 'load-time-2', source: new Source(name: 'source-name-2'), url: 'url-2')
    }

    def 'Post findOneByUrl(String url)'() {
        when:
            Post actual = postDataClient.findOneByUrl('url')

        then:
            1 * postRepository.findOneByUrl('url') >> new Post(id: 1, loadTime: 'load-time', source: new Source(name: 'source-name'), url: 'url')

        expect:
            actual == new Post(id: 1, loadTime: 'load-time', source: new Source(name: 'source-name'), url: 'url')
    }
}
