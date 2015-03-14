package com.github.diplodoc.diploexec.shell

import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class DiploexecShellSpec extends Specification {

    def 'banner'() {
        when:
            Banner banner = new Banner()

        then:
            banner.banner == ''
            banner.version == '0.0.5-SNAPSHOT'
            banner.welcomeMessage == ''
    }

    def 'history'() {
        when:
            History history = new History()

        then:
            history.historyFileName == 'diploexec-shell.history'
    }

    def 'prompt'() {
        when:
            Prompt prompt = new Prompt()

        then:
            prompt.prompt == 'diploexec-shell>'
    }
}
