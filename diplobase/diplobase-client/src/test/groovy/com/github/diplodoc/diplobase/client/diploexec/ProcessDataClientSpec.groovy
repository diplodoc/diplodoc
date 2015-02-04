package com.github.diplodoc.diplobase.client.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRepository
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class ProcessDataClientSpec extends Specification {

    ProcessRepository processRepository = Mock(ProcessRepository)
    ProcessDataClient processDataClient = new ProcessDataClient(processRepository: processRepository)

    def 'Iterable<Process> findAll()'() {
        when:
            def actual = processDataClient.findAll()

        then:
            1 * processRepository.findAll() >> [
                new Process(id: 1, name: 'process-1', lastUpdate: 'time-1'),
                new Process(id: 2, name: 'process-2', lastUpdate: 'time-2')
            ]

        expect:
            actual.size() == 2
            actual[0] == new Process(id: 1, name: 'process-1', lastUpdate: 'time-1')
            actual[1] == new Process(id: 2, name: 'process-2', lastUpdate: 'time-2')
    }

    def 'Process findOneByName(String name)'() {
        when:
            Process actual = processDataClient.findOneByName('process')

        then:
            1 * processRepository.findOneByName('process') >> new Process(id: 1, name: 'process', lastUpdate: 'time')

        expect:
            actual == new Process(id: 1, name: 'process', lastUpdate: 'time')
    }

    def 'Process delete(Process process)'() {
        when:
            processDataClient.delete(new Process(id: 1, name: 'process', lastUpdate: 'time'))

        then:
            1 * processRepository.delete(new Process(id: 1, name: 'process', lastUpdate: 'time'))
    }

    def 'Process save(Process process)'() {
        when:
            Process actual = processDataClient.save(new Process(name: 'process', lastUpdate: 'time'))

        then:
            1 * processRepository.save(new Process(name: 'process', lastUpdate: 'time')) >> new Process(id: 1, name: 'process', lastUpdate: 'time')

        expect:
            actual == new Process(id: 1, name: 'process', lastUpdate: 'time')
    }
}
