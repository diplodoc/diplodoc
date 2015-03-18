package com.github.diplodoc.diplobase.shell

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
        '0.0.5'
    }

    @Override
    String getWelcomeMessage() {
        ''
    }
}