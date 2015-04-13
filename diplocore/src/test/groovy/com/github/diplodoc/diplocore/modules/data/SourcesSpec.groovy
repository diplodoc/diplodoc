package com.github.diplodoc.diplocore.modules.data

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethod
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.SourceRepository
import com.github.diplodoc.diplocore.services.AuditService
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SourcesSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)
    AuditService auditService = Mock(AuditService)

    Sources sources = new Sources(sourceRepository: sourceRepository, auditService: auditService)

    def 'def all()'() {
        when:
            1 * auditService.runMethodUnderAudit('com.github.diplodoc.diplocore.modules.data.Sources', 'all', _) >> { it ->
                Module module = new Module()
                ModuleMethod moduleMethod = new ModuleMethod()
                ModuleMethodRun moduleMethodRun = new ModuleMethodRun()

                return it[2].call(module, moduleMethod, moduleMethodRun)
            }

            sourceRepository.findAll() >> [ new Source(id: new ObjectId('111111111111111111111111')), new Source(id: new ObjectId('222222222222222222222222')) ]

        then:
            Map actual = sources.all()

        expect:
            actual.keySet().size() == 2
            actual['result'] == [ '111111111111111111111111', '222222222222222222222222' ]
            actual['metrics'] == [ 'sources count': 2 ]
    }
}
