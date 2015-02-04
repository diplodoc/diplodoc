package com.github.diplodoc.diplobase.client.diplodata

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class PostDataClient {

    @Autowired
    PostRepository postRepository

    Iterable<Post> findAllWithLimit(int limit) {
        postRepository.findAll(new PageRequest(0, limit, Sort.Direction.DESC, 'id'))
    }

    Post findOneByUrl(String url) {
        postRepository.findOneByUrl(url)
    }
}
