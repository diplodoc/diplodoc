package com.github.dipodoc.diploweb.controller.diploexec

import com.github.dipodoc.diploweb.controller.diploexec.ModuleMethodRunController
import com.github.dipodoc.diploweb.diploexec.Module
import com.github.dipodoc.diploweb.diploexec.ModuleMethod
import com.github.dipodoc.diploweb.diploexec.ModuleMethodRun
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ModuleMethodRunController)
@Mock([ Module, ModuleMethod, ModuleMethodRun])
class ModuleMethodRunControllerSpec extends Specification {

    def "'list' action"() {
        given: 'two domain instances'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            ModuleMethod moduleMethod = new ModuleMethod(name: 'name', module: module).save flush:true
            ModuleMethodRun moduleMethodRun1 = new ModuleMethodRun(startTime: '1', endTime: '2', metrics: [], moduleMethod: moduleMethod).save flush:true
            ModuleMethodRun moduleMethodRun2 = new ModuleMethodRun(startTime: '3', endTime: '4', metrics: [], moduleMethod: moduleMethod).save flush:true

        when: 'action is executed'
            controller.list()

        then: 'model contains both instances sorted by startTime desc'
            model.moduleMethodRunInstanceCount == 2
            model.moduleMethodRunInstanceList == [ moduleMethodRun2, moduleMethodRun1 ]
    }

    def "'list' action with pagination"() {
        given: 'two domain instances'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            ModuleMethod moduleMethod = new ModuleMethod(name: 'name', module: module).save flush:true
            ModuleMethodRun moduleMethodRun1 = new ModuleMethodRun(startTime: '1', endTime: '2', metrics: [], moduleMethod: moduleMethod).save flush:true
            ModuleMethodRun moduleMethodRun2 = new ModuleMethodRun(startTime: '3', endTime: '4', metrics: [], moduleMethod: moduleMethod).save flush:true

        when: 'action is executed with max=1 parameter'
            controller.list(1)

        then: 'model contains one of instances, total instances count is 2'
            model.moduleMethodRunInstanceCount == 2
            model.moduleMethodRunInstanceList == [ moduleMethodRun1 ] || model.moduleMethodRunInstanceList == [ moduleMethodRun2 ]
    }

    def "'show' action"() {
        when: 'domain instance is passed to the action'
            Module module = new Module(name: 'name', data: [:]).save flush:true
            ModuleMethod moduleMethod = new ModuleMethod(name: 'name', module: module).save flush:true
            ModuleMethodRun moduleMethodRun = new ModuleMethodRun(startTime: '1', endTime: '2', metrics: [], moduleMethod: moduleMethod).save flush:true
            controller.show(moduleMethodRun)

        then: 'model contains this instance'
            model.moduleMethodRunInstance == moduleMethodRun
    }

    def "'show' action with null domain"() {
        when: 'action is executed with a null domain'
            controller.show(null)

        then: 'A 404 error is returned'
            response.status == 404
    }
}
