package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplocore.modules.support.JythonIntegrationSupport

/**
 * @author yaroslav.yermilov
 */
class TestActor extends JythonIntegrationSupport implements Module {

    @Override
    void bindSelf(Binding binding) {
        binding.modify = instance().&modify
    }

    Object modify(Expando obj) {}
}