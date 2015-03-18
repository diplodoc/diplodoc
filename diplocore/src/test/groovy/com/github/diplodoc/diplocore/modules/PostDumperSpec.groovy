package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostDumperSpec extends Specification {

    PostDumper postDumper = new PostDumper()

    def 'String toDump(Post post)'() {
        when:
            Post post = new Post(id: 'id', url: 'url', title: 'title', meaningText: 'meaning-text')

        then:
            List<String> actual = postDumper.toDump(post)

        expect:
            actual == [ 'id', 'url', 'title', 'meaning-text' ]
    }
}
