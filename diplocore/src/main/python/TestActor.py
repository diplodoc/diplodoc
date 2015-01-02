from com.github.diplodoc.diplocore.modules import TestActor

class TestActor(TestActor):
    def modify(self, object):
        object.setProperty('data', 'just testing...')
        return object