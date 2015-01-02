package com.github.diplodoc.diplocore.modules.support

import org.python.util.PythonInterpreter
import org.springframework.context.ResourceLoaderAware
import org.springframework.core.io.ResourceLoader

/**
 * @author yaroslav.yermilov
 */
class JythonIntegrationSupport implements ResourceLoaderAware {

    ResourceLoader resourceLoader

    def module = getClass().simpleName
    def moduleInterface = getClass()

    def instance() {
        PythonInterpreter interpreter = new PythonInterpreter()
        interpreter.execfile(resourceLoader.getResource("classpath:${module}.py").inputStream)
        interpreter.exec("from ${module} import ${module}")

        def buildingClass = interpreter.get(module)

        return buildingClass.__call__().__tojava__(moduleInterface)
    }
}
