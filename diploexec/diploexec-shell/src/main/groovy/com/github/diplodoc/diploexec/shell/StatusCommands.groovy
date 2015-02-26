package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.client.diploexec.ProcessRunDataClient
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRunParameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class StatusCommands implements CommandMarker {

    @Autowired
    ProcessRunDataClient processRunDataClient

    @CliCommand(value = 'status', help = 'current diploexec runtime status')
    String status(@CliOption(key = 'count', mandatory = false, help = 'number of last runs to show', unspecifiedDefaultValue = '10') final Integer count) {
        processRunDataClient.all(count).collect(StatusCommands.&toDescription).join('\n')
    }

    private static toDescription(ProcessRun processRun) {
        'id:'.padRight(20) + processRun.id + '\n' +
        'process:'.padRight(20) + processRun.process.name + '\n' +
        'status:'.padRight(20) + processRun.exitStatus + '\n' +
        'start time:'.padRight(20) + processRun.startTime + '\n' +
        'end time:'.padRight(20) + processRun.endTime + '\n' +
        ((!processRun.parameters.isEmpty()) ? 'parameters:\n' + processRun.parameters.collect(StatusCommands.&toDescription).join('\n') : '')
    }

    private static toDescription(ProcessRunParameter processRunParameter) {
        '    key:'.padRight(20) + "${processRunParameter.key}\n" +
        '    type:'.padRight(20) + "${processRunParameter.type}\n" +
        '    value:'.padRight(20) + "${processRunParameter.value}"
    }
}
