package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.HtmlService
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

            docRepository.findOne('id') >> new Doc(id: 'id', uri: 'uri')
            htmlService.load('uri') >> document

            docLoader.loadDoc('id')

        then:
            1 * docRepository.save({ Doc doc ->
                doc.id == 'id' &&
                doc.uri == 'uri' &&
                doc.binary == 'html'.bytes &&
                doc.loadTime != null
            })
    }
}
