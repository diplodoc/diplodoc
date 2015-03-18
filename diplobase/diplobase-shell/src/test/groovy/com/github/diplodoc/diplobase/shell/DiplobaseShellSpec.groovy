package com.github.diplodoc.diplobase.shell

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class DiplobaseShellSpec extends Specification {

    def 'banner'() {
        when:
            Banner banner = new Banner()

        then:
            banner.banner == ''
            banner.version == '0.0.5'
            banner.welcomeMessage == ''
    }

    def 'history'() {
        when:
            History history = new History()

        then:
            history.historyFileName == 'diplobase-shell.history'
    }

    def 'prompt'() {
        when:
            Prompt prompt = new Prompt()

        then:
            prompt.prompt == 'diplobase-shell>'
    }
}
