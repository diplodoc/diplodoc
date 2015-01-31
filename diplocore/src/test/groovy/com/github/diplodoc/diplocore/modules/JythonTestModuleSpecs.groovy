package com.github.diplodoc.diplocore.modules

import org.springframework.core.io.FileSystemResourceLoader
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class JythonTestModuleSpecs extends Specification {

    def 'test jython module'() {
        when:
            JythonTestModule jythonTestModule = new JythonTestModule()
            jythonTestModule.resourceLoader = new FileSystemResourceLoader()

            Expando object = new Expando()
            object.data = 'some value'

        then:
            Expando actual = jythonTestModule.instance().modify(object)

        expect:
            actual.data == 'just testing...'
    }
}
