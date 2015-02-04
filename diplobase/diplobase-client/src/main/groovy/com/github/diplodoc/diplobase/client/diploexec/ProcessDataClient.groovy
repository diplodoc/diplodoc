package com.github.diplodoc.diplobase.client.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class ProcessDataClient {

    @Autowired
    ProcessRepository processRepository

    Iterable<Process> findAll() {
        processRepository.findAll()
    }

    Process findOneByName(String name) {
        processRepository.findOneByName(name)
    }

    void delete(Process process) {
        processRepository.delete(process)
    }

    Process save(Process process) {
        processRepository.save(process)
    }
}
