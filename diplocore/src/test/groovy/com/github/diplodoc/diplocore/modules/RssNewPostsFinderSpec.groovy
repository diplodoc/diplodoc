package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Post
import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.SourceRepository
import com.github.diplodoc.diplocore.services.RssService
import com.rometools.rome.feed.synd.SyndContentImpl
import com.rometools.rome.feed.synd.SyndEntry
import org.bson.types.ObjectId
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
class RssNewPostsFinderSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)
    PostRepository postRepository = Mock(PostRepository)
    RssService rssService = Mock(RssService)

    RssNewPostsFinder rssNewPostsFinder = new RssNewPostsFinder(sourceRepository: sourceRepository, postRepository: postRepository, rssService: rssService)

    def 'List<String> newPosts(String sourceId)'() {
        when:
            SyndEntry rssEntry1 = Mock(SyndEntry)
            rssEntry1.link >> 'link-1'

            SyndEntry rssEntry2 = Mock(SyndEntry)
            rssEntry2.link >> 'link-2'
            rssEntry2.title >> 'title-2'
            rssEntry2.description >> new SyndContentImpl(value: 'description-2')
            rssEntry2.publishedDate >> new Date(2000000)

            SyndEntry rssEntry3 = Mock(SyndEntry)
            rssEntry3.link >> 'link-3'
            rssEntry3.title >> 'title-3'
            rssEntry3.description >> new SyndContentImpl(value: 'description-3')
            rssEntry3.publishedDate >> new Date(3000000)

            sourceRepository.findOne('111111111111111111111111') >> new Source(id: '111111111111111111111111', rssUrl: 'rss-url')

            postRepository.findOneByUrl('link-1') >> new Post()
            postRepository.findOneByUrl('link-2') >> null
            postRepository.findOneByUrl('link-3') >> null

            rssService.feed('rss-url') >> [ rssEntry1, rssEntry2, rssEntry3 ]

            Collection<String> actual = rssNewPostsFinder.newPosts('111111111111111111111111')

        then:
            1 * postRepository.save({ posts ->
                posts.find { it.url == 'link-2' }.id = 'id-2'
                posts.find { it.url == 'link-3' }.id = 'id-3'

                posts == [
                    new Post(id: 'id-2', url: 'link-2', sourceId: new ObjectId('111111111111111111111111'), title: 'title-2', description: 'description-2', publishTime: LocalDateTime.parse('1970-01-01T02:33:20')),
                    new Post(id: 'id-3', url: 'link-3', sourceId: new ObjectId('111111111111111111111111'), title: 'title-3', description: 'description-3', publishTime: LocalDateTime.parse('1970-01-01T02:50'))
                ]
            })

        expect:
            actual == [ 'id-2', 'id-3' ]
    }
}
