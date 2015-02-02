package com.github.diplodoc.diploexec.shell

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRunParameter
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRunRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class StatusCommandsSpec extends Specification {

    ProcessRunRepository processRunRepository = Mock(ProcessRunRepository)
    StatusCommands statusCommands = new StatusCommands(processRunRepository: processRunRepository)

    def '`status` command with default option'() {
        when:
            processRunRepository.findAll(new PageRequest(0, 10, Sort.Direction.DESC, 'startTime')) >> new PageImpl<ProcessRun>([
                new ProcessRun(id: 1, process: new Process(name: 'process-1'), exitStatus: 'status', startTime: 'starttime-1', endTime: 'endtime-1', parameters: [ new ProcessRunParameter(key: 'key', type: 'type', value: 'value') ]),
                new ProcessRun(id: 2, process: new Process(name: 'process-2'), exitStatus: 'status', startTime: 'starttime-2', endTime: 'endtime-2', parameters: [ ])
            ])

        then:
            String actual = statusCommands.status(null)

        expect:
            actual ==   'id:                 1\n' +
                        'process:            process-1\n' +
                        'status:             status\n' +
                        'start time:         starttime-1\n' +
                        'end time:           endtime-1\n' +
                        'parameters:\n' +
                        '    key:            key\n' +
                        '    type:           type\n' +
                        '    value:          value\n' +
                        'id:                 2\n' +
                        'process:            process-2\n' +
                        'status:             status\n' +
                        'start time:         starttime-2\n' +
                        'end time:           endtime-2\n'
    }

    def '`status` command with count option'() {
        when:
            processRunRepository.findAll(new PageRequest(0, 1, Sort.Direction.DESC, 'startTime')) >> new PageImpl<ProcessRun>([
                new ProcessRun(id: 1, process: new Process(name: 'process-1'), exitStatus: 'status', startTime: 'starttime-1', endTime: 'endtime-1', parameters: [ new ProcessRunParameter(key: 'key', type: 'type', value: 'value') ]),
            ])

        then:
            String actual = statusCommands.status(1)

        expect:
            actual ==   'id:                 1\n' +
                        'process:            process-1\n' +
                        'status:             status\n' +
                        'start time:         starttime-1\n' +
                        'end time:           endtime-1\n' +
                        'parameters:\n' +
                        '    key:            key\n' +
                        '    type:           type\n' +
                        '    value:          value'
    }
}
