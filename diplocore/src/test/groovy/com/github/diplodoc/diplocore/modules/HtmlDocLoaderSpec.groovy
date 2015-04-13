package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethod
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.HtmlService
import org.bson.types.ObjectId
import org.jsoup.nodes.Document
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class HtmlDocLoaderSpec extends Specification {

    DocRepository docRepository = Mock(DocRepository)
    HtmlService htmlService = Mock(HtmlService)
    AuditService auditService = Mock(AuditService)

    HtmlDocLoader htmlDocLoader = new HtmlDocLoader(docRepository: docRepository, htmlService: htmlService, auditService: auditService)

    def 'def loadDoc(String docId)'() {
        when:
            1 * auditService.runMethodUnderAudit('com.github.diplodoc.diplocore.modules.HtmlDocLoader', 'loadDoc', _) >> { it ->
                Module module = new Module()
                ModuleMethod moduleMethod = new ModuleMethod()
                ModuleMethodRun moduleMethodRun = new ModuleMethodRun()

                return it[2].call(module, moduleMethod, moduleMethodRun)
            }

            Document document = Mock(Document)
            document.html() >> 'html'

            docRepository.findOne(new ObjectId('111111111111111111111111')) >> new Doc(id: new ObjectId('111111111111111111111111'), uri: 'uri')
            htmlService.load('uri') >> document

            Map actual = htmlDocLoader.loadDoc('111111111111111111111111')

        then:
            1 * docRepository.save({ Doc doc ->
                doc.id == new ObjectId('111111111111111111111111') &&
                doc.uri == 'uri' &&
                doc.html == 'html' &&
                doc.loadTime != null
            })

            actual.keySet().size() == 1
            actual['moduleMethodRun'].parameters == [ 'docId': '111111111111111111111111' ]
    }
}
