package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostsCommandsSpec extends Specification {

    PostRepository postRepository = Mock(PostRepository)
    PostsCommands postsCommands = new PostsCommands(postRepository: postRepository)

    def '`posts list` command with default option'() {
        when:
            postRepository.findAll(_) >> new PageImpl<Post>([
                new Post(id: 1, loadTime: 'load-time-1', source: new Source(name: 'source-name-1'), url: 'url-1'),
                new Post(id: 2, loadTime: 'load-time-2', source: new Source(name: 'source-name-2'), url: 'url-2')
            ])

        then:
            postsCommands.list(null) == '1    load-time-1                   source-name-1       url-1\n' +
                                        '2    load-time-2                   source-name-2       url-2'
    }
}
