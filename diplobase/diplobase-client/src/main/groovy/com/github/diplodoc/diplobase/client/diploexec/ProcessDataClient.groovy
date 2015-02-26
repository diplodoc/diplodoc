package com.github.diplodoc.diplobase.client.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.Process
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class ProcessDataClient {

    @Autowired
    ProcessRepository processRepository

    Collection<Process> all() {
        processRepository.findAll()
    }

    Process byName(String name) {
        processRepository.findOneByName(name)
    }

    Collection<Process> tests() {
        processRepository.findByNameLike('test%')
    }

    Process save(Process process) {
        processRepository.save(process)
    }
}
