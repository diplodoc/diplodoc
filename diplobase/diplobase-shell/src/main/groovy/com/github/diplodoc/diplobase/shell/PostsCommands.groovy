package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.client.diplodata.PostDataClient
import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

import java.util.regex.Pattern

/**
 * @author yaroslav.yermilov
 */
@Component
class PostsCommands implements CommandMarker {

    @Autowired
    PostRepository postRepository

    @Autowired
    TopicRepository topicRepository

    @CliCommand(value = 'posts list', help = 'list all posts')
    String list(@CliOption(key = 'count', mandatory = false, help = 'number of last posts to show', unspecifiedDefaultValue = '10') final Integer count) {
        postRepository.findAll(new PageRequest(0, count, Sort.Direction.DESC, 'loadTime')).collect(PostsCommands.&toSingleLineDescription).join('\n')
    }

    @CliCommand(value = 'posts get', help = 'get full description of post')
    String get(@CliOption(key = '', mandatory = true, help = 'post url') final String url) {
        toDescription(postRepository.findOneByUrl(url))
    }

    @CliCommand(value = 'posts load-dumps', help = 'load posts dumps')
    String loadDumps(@CliOption(key = '', mandatory = true, help = 'path') final String path) {
        new File(path).listFiles().findAll({it.name.contains('post-')}).each { File file ->
            List lines = file.text.readLines()
            String id = lines[0]
            String url = lines[1]
            String title = lines[2]
            String[] topics = lines[3].split(Pattern.quote(','))
            String meaningText = lines[4..-1].join('\n')

            Post post = postRepository.findOne id
            post.url = url
            post.title = title
            post.meaningText = meaningText
            post.train_topics = topics.collect { topicRepository.findOneByLabel(it.trim()) }

            postRepository.save post
        }
        'Done'
    }

    static String toSingleLineDescription(Post post) {
        "${post.id}".padRight(5) + "${post.loadTime}".padRight(30) + "${post.source.name}".padRight(20) + "${post.url}"
    }

    static String toDescription(Post post) {
        'id:'.padRight(20) + "${post.id}\n" +
        'source:'.padRight(20) + "${post.source.name}\n" +
        'load time:'.padRight(20) + "${post.loadTime}\n" +
        'publish time:'.padRight(20) + "${post.publishTime}\n" +
        'url:'.padRight(20) + "${post.url}\n" +
        'title:'.padRight(20) + "${post.title}\n" +
        'description:'.padRight(20) + "${post.description}\n" +
        'meaning text:\n' + "${post.meaningText}"
    }
}
