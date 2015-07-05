package com.github.diplodoc.modules.services

import com.github.diplodoc.domain.mongodb.orchestration.Module
import com.github.diplodoc.domain.mongodb.orchestration.ModuleMethod
import com.github.diplodoc.domain.mongodb.orchestration.ModuleMethodRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ModuleMethodRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ModuleMethodRunRepository
import com.github.diplodoc.domain.repository.mongodb.orchestration.ModuleRepository
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

    def 'def runMethodUnderAudit(String moduleName, String methodName, Closure originalMethod) - module and moduleMethod does not exists'() {
        given:
            Module module1 = new Module(id: new ObjectId('111111111111111111111111'), name: 'moduleName-1')
            Module module2 = new Module(id: new ObjectId('222222222222222222222222'), name: 'moduleName-2')
            ModuleMethod moduleMethod1 = new ModuleMethod(id: new ObjectId('333333333333333333333333'), name: 'methodName-1', moduleId: module1.id)
            ModuleMethod moduleMethod2 = new ModuleMethod(id: new ObjectId('444444444444444444444444'), name: 'methodName-1', moduleId: module2.id)

            def method = { module, moduleMethod, moduleMethodRun ->
                [ 'result': 42, 'metrics': [ 'key': 'value' ], 'module': module, 'moduleMethod': moduleMethod ]
            }

        when:
            2 * moduleRepository.save(_) >> module1
            2 * moduleMethodRepository.save(_) >> moduleMethod1

            def actual = auditService.runMethodUnderAudit('moduleName-1', 'methodName-1', method )

        then:
            1 * moduleMethodRunRepository.save({ ModuleMethodRun moduleMethodRunToSave ->
                moduleMethodRunToSave.moduleMethodId == new ObjectId('333333333333333333333333') &&
                        moduleMethodRunToSave.startTime != null &&
                        moduleMethodRunToSave.endTime != null &&
                        moduleMethodRunToSave.metrics == [ 'key': 'value' ]
            })

            actual == 42
    }

    def 'def runMethodUnderAudit(String moduleName, String methodName, Closure originalMethod) - do not return module and moduleMethod'() {
        given:
            Module module1 = new Module(id: new ObjectId('111111111111111111111111'), name: 'moduleName-1')
            Module module2 = new Module(id: new ObjectId('222222222222222222222222'), name: 'moduleName-2')
            ModuleMethod moduleMethod1 = new ModuleMethod(id: new ObjectId('333333333333333333333333'), name: 'methodName-1', moduleId: module1.id)
            ModuleMethod moduleMethod2 = new ModuleMethod(id: new ObjectId('444444444444444444444444'), name: 'methodName-1', moduleId: module2.id)

            def method = { module, moduleMethod, moduleMethodRun ->
                [ 'result': 42, 'metrics': [ 'key': 'value' ] ]
            }

        when:
            1 * moduleRepository.findOneByName('moduleName-1') >> module1
            1 * moduleMethodRepository.findByName('methodName-1') >> [ moduleMethod1, moduleMethod2 ]

            def actual = auditService.runMethodUnderAudit('moduleName-1', 'methodName-1', method )

        then:
            0 * moduleRepository.save(module1)
            0 * moduleMethodRepository.save(moduleMethod1)
            1 * moduleMethodRunRepository.save({ ModuleMethodRun moduleMethodRunToSave ->
                moduleMethodRunToSave.moduleMethodId == new ObjectId('333333333333333333333333') &&
                        moduleMethodRunToSave.startTime != null &&
                        moduleMethodRunToSave.endTime != null &&
                        moduleMethodRunToSave.metrics == [ 'key': 'value' ]
            })

            actual == 42
    }
}
