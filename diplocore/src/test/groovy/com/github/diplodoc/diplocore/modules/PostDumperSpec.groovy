package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class PostDumperSpec extends Specification {

    def 'String toDump(Post post)'() {
        when:
            Post post = new Post(id: 'id', url: 'url', title: 'title', meaningText: 'meaning-text')

        then:
            String actual = PostDumper.toDump(post)

        expect:
            actual == 'id\nurl\ntitle\nmeaning-text'
    }
}
