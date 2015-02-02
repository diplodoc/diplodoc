package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class PostsCommands implements CommandMarker {

    @Autowired
    PostRepository postRepository

    @CliCommand(value = 'posts list', help = 'list all posts')
    String list(@CliOption(key = 'count', mandatory = false, help = 'number of last posts to show') final Integer count) {
        Iterable<Post> lastPosts = postRepository.findAll(new PageRequest(0, count?:10, Sort.Direction.DESC, 'id'))

        lastPosts.findAll().collect { Post post ->
            "${post.id}".padRight(5) +
            "${post.loadTime}".padRight(30) +
            "${post.source.name}".padRight(20) +
            "${post.url}"
        }.join('\n')
    }

    @CliCommand(value = 'posts get', help = 'get full description of post')
    String get(@CliOption(key = '', mandatory = true, help = 'post url') final String url) {
        Post post = postRepository.findOneByUrl(url)

        'id:'.padRight(20) + "${post.id}\n" +
        'source:'.padRight(20) + "${post.source.name}\n" +
        'load time:'.padRight(20) + "${post.loadTime}\n" +
        'url:'.padRight(20) + "${post.url}\n" +
        'title:'.padRight(20) + "${post.title}\n" +
        'meaning text:\n' + "${post.meaningText}"
    }
}
