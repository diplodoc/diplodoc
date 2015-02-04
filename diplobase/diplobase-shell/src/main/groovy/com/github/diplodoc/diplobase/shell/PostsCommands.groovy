package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.client.diplodata.PostDataClient
import com.github.diplodoc.diplobase.domain.diplodata.Post
import org.springframework.beans.factory.annotation.Autowired
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
    PostDataClient postDataClient

    @CliCommand(value = 'posts list', help = 'list all posts')
    String list(@CliOption(key = 'count', mandatory = false, help = 'number of last posts to show', unspecifiedDefaultValue = '10') final Integer count) {
        postDataClient.findAllWithLimit(count).collect(PostsCommands.&toSingleLineDescription).join('\n')
    }

    @CliCommand(value = 'posts get', help = 'get full description of post')
    String get(@CliOption(key = '', mandatory = true, help = 'post url') final String url) {
        toDescription(postDataClient.findOneByUrl(url))
    }

    static String toSingleLineDescription(Post post) {
        "${post.id}".padRight(5) +
        "${post.loadTime}".padRight(30) +
        "${post.source.name}".padRight(20) +
        "${post.url}"
    }

    static String toDescription(Post post) {
        'id:'.padRight(20) + "${post.id}\n" +
        'source:'.padRight(20) + "${post.source.name}\n" +
        'load time:'.padRight(20) + "${post.loadTime}\n" +
        'url:'.padRight(20) + "${post.url}\n" +
        'title:'.padRight(20) + "${post.title}\n" +
        'meaning text:\n' + "${post.meaningText}"
    }
}
