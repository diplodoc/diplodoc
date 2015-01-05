package com.github.diplodoc.diploexec.shell

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.shell.plugin.support.DefaultPromptProvider
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class Prompt extends DefaultPromptProvider {

    @Override
    String getPrompt() {
        return "diploexec-shell>";
    }
}
