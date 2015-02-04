package com.github.diplodoc.diplobase.client.diploexec

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.repository.diploexec.ProcessRunRepository
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

    Iterable<ProcessRun> findAllWithLimit(int limit) {
        processRunRepository.findAll(new PageRequest(0, limit, Sort.Direction.DESC, 'startTime'))
    }
}
