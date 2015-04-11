package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.HtmlService
import org.bson.types.ObjectId
import org.jsoup.nodes.Document
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class DocLoaderSpec extends Specification {

    DocRepository docRepository = Mock(DocRepository)
    HtmlService htmlService = Mock(HtmlService)

    DocLoader docLoader = new DocLoader(docRepository: docRepository, htmlService: htmlService)

    def 'void loadDoc(String docId)'() {
        when:
            Document document = Mock(Document)
            document.html() >> 'html'

            docRepository.findOne(new ObjectId('111111111111111111111111')) >> new Doc(id: new ObjectId('111111111111111111111111'), uri: 'uri')
            htmlService.load('uri') >> document

            docLoader.loadDoc('111111111111111111111111')

        then:
            1 * docRepository.save({ Doc doc ->
                doc.id == new ObjectId('111111111111111111111111') &&
                doc.uri == 'uri' &&
                doc.binary == 'html'.bytes &&
                doc.loadTime != null
            })
    }
}
