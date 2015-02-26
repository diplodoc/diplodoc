package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.client.diplodata.PostDataClient
import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Source
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostsCommandsSpec extends Specification {

    PostDataClient postDataClient = Mock(PostDataClient)
    PostsCommands postsCommands = new PostsCommands(postDataClient: postDataClient)

    def '`posts list`'() {
        when:
            postDataClient.all(5) >> [
                new Post(id: 1, loadTime: 'load-time-1', source: new Source(name: 'source-name-1'), url: 'url-1'),
                new Post(id: 2, loadTime: 'load-time-2', source: new Source(name: 'source-name-2'), url: 'url-2')
            ]

        then:
            String actual = postsCommands.list(5)

        expect:
            actual == '1    load-time-1                   source-name-1       url-1\n' +
                      '2    load-time-2                   source-name-2       url-2'
    }

    def '`posts get` command'() {
        when:
            postDataClient.byUrl('url') >> new Post(
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
}
