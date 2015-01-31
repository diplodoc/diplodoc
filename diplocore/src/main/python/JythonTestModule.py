from com.github.diplodoc.diplocore.modules import JythonTestModule

class JythonTestModule(JythonTestModule):
    def modify(self, object):
        object.setProperty('data', 'just testing...')
        return object