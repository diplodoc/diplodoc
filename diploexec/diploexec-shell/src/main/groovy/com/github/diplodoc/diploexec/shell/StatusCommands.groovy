package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRunParameter
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRunRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
    ProcessRunRepository processRunRepository

    @CliCommand(value = 'status', help = 'current diploexec runtime status')
    String status(@CliOption(key = 'count', mandatory = false, help = 'number of last runs to show') final Integer count) {
        Iterable<ProcessRun> lastProcessRuns = processRunRepository.findAll(new PageRequest(0, count?:10, Sort.Direction.DESC, 'startTime'))
        lastProcessRuns.collect(StatusCommands.&longToString).join('\n')
    }

    private static longToString(ProcessRun processRun) {
        'id:'.padRight(20) + processRun.id + '\n' +
        'process:'.padRight(20) + processRun.process.name + '\n' +
        'start time:'.padRight(20) + processRun.startTime + '\n' +
        'end time:'.padRight(20) + processRun.endTime + '\n' +
        ((!processRun.parameters.isEmpty()) ? 'parameters:\n' + processRun.parameters.collect(ProcessCommands.&longToString).join('\n') : '')
    }

    private static longToString(ProcessRunParameter processRunParameter) {
        '    key:'.padRight(20) + "${processRunParameter.key}\n" +
        '    type:'.padRight(20) + "${processRunParameter.type}\n" +
        '    value:'.padRight(20) + "${processRunParameter.value}"
    }
}
