package com.github.diplodoc.diplobase.client.diplodata

import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
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
            postDataClient.findAllWithLimit(28)

        then:
            1 * postRepository.findAll(new PageRequest(0, 28, Sort.Direction.DESC, 'id'))
    }

    def 'Post findOneByUrl(String url)'() {
        when:
            postDataClient.findOneByUrl('url')

        then:
            1 * postRepository.findOneByUrl('url')
    }
}
