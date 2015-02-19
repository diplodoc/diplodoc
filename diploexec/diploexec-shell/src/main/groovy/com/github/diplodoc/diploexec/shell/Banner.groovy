package com.github.diplodoc.diploexec.shell

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.shell.plugin.support.DefaultBannerProvider
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class Banner extends DefaultBannerProvider {

    @Override
    String getBanner() {
        ''
    }

    @Override
    String getVersion() {
        '0.0.4-SNAPSHOT'
    }

    @Override
    String getWelcomeMessage() {
        ''
    }
}