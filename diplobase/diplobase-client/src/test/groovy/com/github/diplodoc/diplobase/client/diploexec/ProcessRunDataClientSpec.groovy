package com.github.diplodoc.diplobase.client.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRunParameter
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRunRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessRunDataClientSpec extends Specification {

    ProcessRunRepository processRunRepository = Mock(ProcessRunRepository)
    ProcessRunDataClient processRunDataClient = new ProcessRunDataClient(processRunRepository: processRunRepository)

    def 'Iterable<ProcessRun> findAllWithLimit(int limit)'() {
        when:
            def actual = processRunDataClient.findAllWithLimit(5)

        then:
            1 * processRunRepository.findAll(new PageRequest(0, 5, Sort.Direction.DESC, 'startTime')) >> new PageImpl<ProcessRun>([
                new ProcessRun(id: 1, process: new Process(name: 'process-1'), exitStatus: 'status', startTime: 'starttime-1', endTime: 'endtime-1', parameters: [ new ProcessRunParameter(key: 'key', type: 'type', value: 'value') ]),
                new ProcessRun(id: 2, process: new Process(name: 'process-2'), exitStatus: 'status', startTime: 'starttime-2', endTime: 'endtime-2', parameters: [ ])
            ])

        expect:
            actual.size() == 2
            actual[0] == new ProcessRun(id: 1, process: new Process(name: 'process-1'), exitStatus: 'status', startTime: 'starttime-1', endTime: 'endtime-1', parameters: [ new ProcessRunParameter(key: 'key', type: 'type', value: 'value') ])
            actual[1] == new ProcessRun(id: 2, process: new Process(name: 'process-2'), exitStatus: 'status', startTime: 'starttime-2', endTime: 'endtime-2', parameters: [ ])
    }
}
