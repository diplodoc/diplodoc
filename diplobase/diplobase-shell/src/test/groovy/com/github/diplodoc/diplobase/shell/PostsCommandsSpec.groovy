package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.domain.mongodb.Topic
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.TopicRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostsCommandsSpec extends Specification {

    PostRepository postRepository = Mock(PostRepository)
    TopicRepository topicRepository = Mock(TopicRepository)

    PostsCommands postsCommands = new PostsCommands(postRepository: postRepository, topicRepository: topicRepository)

    def '`posts list --count 5`'() {
        when:
            postRepository.findAll(new PageRequest(0, 5, Sort.Direction.DESC, 'loadTime')) >> new PageImpl<Post>([
                new Post(id: '1', loadTime: 'load-time-1', source: new Source(name: 'source-name-1'), url: 'url-1'),
                new Post(id: '2', loadTime: 'load-time-2', source: new Source(name: 'source-name-2'), url: 'url-2')
            ])

        then:
            String actual = postsCommands.list(5)

        expect:
            actual == '1    load-time-1                   source-name-1       url-1\n' +
                      '2    load-time-2                   source-name-2       url-2'
    }

    def '`posts get`'() {
        when:
            postRepository.findOneByUrl('url') >> new Post(
                                                        id: 1,
                                                        loadTime: 'load-time',
                                                        source: new Source(name: 'source-name'),
                                                        url: 'url',
                                                        title: 'title',
                                                        meaningText: 'meaning text',
                                                        publishTime: 'publish-time',
                                                        description: 'description'
                                                    )

        then:
            String actual = postsCommands.get('url')

        expect:
            actual ==   'id:                 1\n' +
                        'source:             source-name\n' +
                        'load time:          load-time\n' +
                        'publish time:       publish-time\n' +
                        'url:                url\n' +
                        'title:              title\n' +
                        'description:        description\n' +
                        'meaning text:\n' +
                        'meaning text'
    }

    def 'Post loadDump(List<String> lines)'() {
        when:
            postRepository.findOne('id') >> new Post(id: 'id')

            topicRepository.findOneByLabel('topic-1') >> new Topic(label: 'topic-1')
            topicRepository.findOneByLabel('topic-2') >> new Topic(label: 'topic-2')

            List<String> lines = [  'id',
                                    'url',
                                    'title',
                                    'topic-1,topic-2',
                                    'meaning-line-1',
                                    'meaning-line-2'
            ]

        then:
            Post actual = postsCommands.loadDump(lines)

        expect:
            actual == new Post(id: 'id', url: 'url', title: 'title', train_topics: [ new Topic(label: 'topic-1'), new Topic(label: 'topic-2') ], meaningText: 'meaning-line-1\nmeaning-line-2')
    }

    def 'static String toSingleLineDescription(Post post)'() {
        when:
            Post post = new Post(id: '1', loadTime: 'load-time-1', source: new Source(name: 'source-name-1'), url: 'url-1')

        then:
            String actual = PostsCommands.toSingleLineDescription(post)

        expect:
            actual == '1    load-time-1                   source-name-1       url-1'
    }

    def 'static String toDescription(Post post)'() {
        when:
            Post post = new Post(
                id: 1,
                loadTime: 'load-time',
                source: new Source(name: 'source-name'),
                url: 'url',
                title: 'title',
                meaningText: 'meaning text',
                publishTime: 'publish-time',
                description: 'description'
            )

        then:
            String actual = PostsCommands.toDescription(post)

        expect:
            actual ==   'id:                 1\n' +
                        'source:             source-name\n' +
                        'load time:          load-time\n' +
                        'publish time:       publish-time\n' +
                        'url:                url\n' +
                        'title:              title\n' +
                        'description:        description\n' +
                        'meaning text:\n' +
                        'meaning text'
    }
}
