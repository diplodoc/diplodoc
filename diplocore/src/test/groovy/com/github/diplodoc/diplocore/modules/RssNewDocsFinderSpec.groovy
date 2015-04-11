package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
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
class RssNewDocsFinderSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)
    DocRepository docRepository = Mock(DocRepository)
    RssService rssService = Mock(RssService)

    RssNewDocsFinder rssNewDocsFinder = new RssNewDocsFinder(sourceRepository: sourceRepository, docRepository: docRepository, rssService: rssService)

    def 'List<String> newDocs(String sourceId)'() {
        when:
            SyndEntry rssEntry1 = Mock(SyndEntry)
            rssEntry1.link >> 'uri-1'

            SyndEntry rssEntry2 = Mock(SyndEntry)
            rssEntry2.link >> 'uri-2'
            rssEntry2.title >> 'title-2'
            rssEntry2.description >> new SyndContentImpl(value: 'description-2')
            rssEntry2.publishedDate >> new Date(2000000)

            SyndEntry rssEntry3 = Mock(SyndEntry)
            rssEntry3.link >> 'uri-3'
            rssEntry3.title >> 'title-3'
            rssEntry3.description >> new SyndContentImpl(value: 'description-3')
            rssEntry3.publishedDate >> new Date(3000000)

            sourceRepository.findOne(new ObjectId('111111111111111111111111')) >> new Source(id: new ObjectId('111111111111111111111111'), rssUrl: 'rss-url')

            docRepository.findOneByUrl('uri-1') >> new Doc()
            docRepository.findOneByUrl('uri-2') >> null
            docRepository.findOneByUrl('uri-3') >> null

            rssService.feed('rss-url') >> [ rssEntry1, rssEntry2, rssEntry3 ]

            Collection<String> actual = rssNewDocsFinder.newDocs('111111111111111111111111')

        then:
            1 * docRepository.save({ docs ->
                docs.find { it.uri == 'uri-2' }.id = new ObjectId('222222222222222222222222')
                docs.find { it.uri == 'uri-3' }.id = new ObjectId('333333333333333333333333')

                docs == [
                    new Doc(id: new ObjectId('222222222222222222222222'), uri: 'uri-2', sourceId: new ObjectId('111111111111111111111111'), title: 'title-2', description: 'description-2', publishTime: LocalDateTime.parse('1970-01-01T02:33:20')),
                    new Doc(id: new ObjectId('333333333333333333333333'), uri: 'uri-3', sourceId: new ObjectId('111111111111111111111111'), title: 'title-3', description: 'description-3', publishTime: LocalDateTime.parse('1970-01-01T02:50'))
                ]
            })

        expect:
            actual == [ '222222222222222222222222', '333333333333333333333333' ]
    }
}
