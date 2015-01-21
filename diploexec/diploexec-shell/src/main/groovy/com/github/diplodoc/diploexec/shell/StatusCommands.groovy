package com.github.diplodoc.diploexec.shell

import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class StatusCommands implements CommandMarker {

    @CliCommand(value = 'status', help = 'current diploexec runtime status')
    String status() {
        assert false : 'not implemented yet'
    }
}
