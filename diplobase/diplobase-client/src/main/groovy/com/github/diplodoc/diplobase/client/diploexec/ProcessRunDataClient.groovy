package com.github.diplodoc.diplobase.client.diploexec

import com.github.diplodoc.diplobase.domain.jpa.diploexec.ProcessRun
import com.github.diplodoc.diplobase.repository.jpa.diploexec.ProcessRunRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class ProcessRunDataClient {

    @Autowired
    ProcessRunRepository processRunRepository

    List<ProcessRun> all(int limit) {
        processRunRepository.findAll(new PageRequest(0, limit, Sort.Direction.DESC, 'startTime'))
    }
}
