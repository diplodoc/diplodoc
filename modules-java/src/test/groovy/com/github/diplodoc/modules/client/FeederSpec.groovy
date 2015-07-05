package com.github.diplodoc.modules.client

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.mongodb.orchestration.Module
import com.github.diplodoc.domain.mongodb.orchestration.ModuleMethod
import com.github.diplodoc.domain.mongodb.orchestration.ModuleMethodRun
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.modules.services.AuditService
import org.bson.types.ObjectId
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class FeederSpec extends Specification {

    DocRepository docRepository = Mock(DocRepository)
    AuditService auditService = Mock(AuditService)

    Feeder feeder = new Feeder(docRepository: docRepository, auditService: auditService)

    def 'def feed(Integer page, Integer size) {'() {
        when:
            1 * auditService.runMethodUnderAudit('client.Feeder', 'feed', _) >> { it ->
                Module module = new Module()
                ModuleMethod moduleMethod = new ModuleMethod()
                ModuleMethodRun moduleMethodRun = new ModuleMethodRun()

                return it[2].call(module, moduleMethod, moduleMethodRun)
            }

            docRepository.findAll(new PageRequest(repositoryPage, repositorySize, new Sort(Sort.Direction.DESC, 'publishTime'))) >> new PageImpl<Doc>([
                new Doc(id: new ObjectId('111111111111111111111111'), uri: 'uri-1', title: 'title-1', publishTime: 'time-1', description: 'description-1'),
                new Doc(id: new ObjectId('222222222222222222222222'), uri: 'uri-2', title: 'title-2', publishTime: 'time-2', description: 'description-2')
            ])

        then:
            Map actual = feeder.feed(paramPage, paramSize)

        expect:
            actual.keySet().size() == 2
            actual['moduleMethodRun'].parameters == [ 'page': paramPage, 'size': paramSize ]
            actual['result'] == [
                [ 'id': '111111111111111111111111', 'url': 'uri-1', 'title': 'title-1', 'time': 'time-1', 'description': 'description-1' ],
                [ 'id': '222222222222222222222222', 'url': 'uri-2', 'title': 'title-2', 'time': 'time-2', 'description': 'description-2' ]
            ]

        where:
            paramPage | paramSize | repositoryPage | repositorySize
            null      | null      | 0              | 20
            2         | null      | 2              | 20
            null      | 30        | 0              | 30
            1         | 50        | 1              | 50
    }
}