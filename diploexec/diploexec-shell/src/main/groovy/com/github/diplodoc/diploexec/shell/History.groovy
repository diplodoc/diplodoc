package com.github.diplodoc.diploexec.shell

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class History extends DefaultHistoryFileNameProvider {

    @Override
    String getHistoryFileName() {
        'diploexec-shell.history'
    }
}