package com.github.diplodoc.diplocore.services

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethod
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class AuditServiceSpec extends Specification {

    ModuleRepository moduleRepository = Mock(ModuleRepository)
    ModuleMethodRepository moduleMethodRepository = Mock(ModuleMethodRepository)
    ModuleMethodRunRepository moduleMethodRunRepository = Mock(ModuleMethodRunRepository)
    AuditService auditService = new AuditService(moduleRepository: moduleRepository, moduleMethodRepository: moduleMethodRepository, moduleMethodRunRepository: moduleMethodRunRepository)

    def 'def runMethodUnderAudit(String moduleName, String methodName, Closure originalMethod)'() {
        given:
            Module module1 = new Module(id: new ObjectId('111111111111111111111111'), name: 'moduleName-1')
            Module module2 = new Module(id: new ObjectId('222222222222222222222222'), name: 'moduleName-2')
            ModuleMethod moduleMethod1 = new ModuleMethod(id: new ObjectId('333333333333333333333333'), name: 'methodName-1', moduleId: module1.id)
            ModuleMethod moduleMethod2 = new ModuleMethod(id: new ObjectId('444444444444444444444444'), name: 'methodName-1', moduleId: module2.id)

            def method = { module, moduleMethod, moduleMethodRun ->
                [ 'result': 42, 'metrics': [ 'key': 'value' ], 'module': module, 'moduleMethod': moduleMethod ]
            }

        when:
            1 * moduleRepository.findOneByName('moduleName-1') >> module1
            1 * moduleMethodRepository.findByName('methodName-1') >> [ moduleMethod1, moduleMethod2 ]

            def actual = auditService.runMethodUnderAudit('moduleName-1', 'methodName-1', method )

        then:
            1 * moduleRepository.save(module1)
            1 * moduleMethodRepository.save(moduleMethod1)
            1 * moduleMethodRunRepository.save({ ModuleMethodRun moduleMethodRunToSave ->
                moduleMethodRunToSave.moduleMethodId == new ObjectId('333333333333333333333333') &&
                moduleMethodRunToSave.startTime != null &&
                moduleMethodRunToSave.endTime != null &&
                moduleMethodRunToSave.metrics == [ 'key': 'value' ]
            })

            actual == 42
    }
}
