package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Rss
import com.rometools.rome.feed.synd.SyndContentImpl
import com.rometools.rome.feed.synd.SyndEntry
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
class RssNewPostFinderSpecs extends Specification {

    Rss rss = Mock(Rss)
    PostRepository postRepository = Mock(PostRepository)
    RssNewPostFinder rssNewPostFinder = new RssNewPostFinder(rss: rss, postRepository: postRepository)

    def 'void findNewPosts(Source source, Closure action)'() {
        given:
            Source source = new Source(rssUrl: 'rss-url')
            Closure action = Mock(Closure)
            SyndEntry syndEntry1 = Mock(SyndEntry)
            SyndEntry syndEntry2 = Mock(SyndEntry)
            SyndEntry syndEntry3 = Mock(SyndEntry)

        when:
            syndEntry1.link >> 'link-1'
            syndEntry1.title >> 'title-1'
            syndEntry1.description >> new SyndContentImpl(value: 'description-1')
            syndEntry1.publishedDate >> new Date(100)

            syndEntry2.link >> 'link-2'
            syndEntry2.title >> 'title-2'
            syndEntry2.description >> new SyndContentImpl(value: 'description-2')
            syndEntry2.publishedDate >> new Date(200)

            syndEntry3.link >> 'link-3'
            syndEntry3.title >> 'title-3'
            syndEntry3.description >> new SyndContentImpl(value: 'description-3')
            syndEntry3.publishedDate >> new Date(300)

            1 * rss.feed('rss-url') >> [ syndEntry1, syndEntry2, syndEntry3 ]

            1 * postRepository.findOneByUrl('link-1') >> null
            1 * postRepository.findOneByUrl('link-2') >> new Post()
            1 * postRepository.findOneByUrl('link-3') >> null

            1 * action.call(new Post(url: 'link-1', source: source, title: 'title-1', description: 'description-1', publishTime: LocalDateTime.parse('1970-01-01T02:00:00.100')))
            1 * action.call(new Post(url: 'link-3', source: source, title: 'title-3', description: 'description-3', publishTime: LocalDateTime.parse('1970-01-01T02:00:00.300')))

        then:
            rssNewPostFinder.findNewPosts(source, action)
    }
}
