package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplocore.modules.support.JythonIntegrationSupport
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('jython-test')
class TestActor extends JythonIntegrationSupport implements Bindable {

    @Override
    void bindSelf(Binding binding) {
        binding.modify = instance().&modify
    }

    Object modify(Expando obj) {}
}