package com.github.diplodoc.initializer.controllers

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@RestController
@Slf4j
class ProcessesInitializer {

    @Autowired
    ProcessRepository processRepository

    @RequestMapping(value = '/processes/init', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void init() {
        log.info 'Going to delete all processes...'
        processRepository.deleteAll()

        log.info 'Reading init processes list...'
        String processesJson = getClass().getResource('/processes.json').text

        log.info "Parsing init processes list:\n${processesJson}"
        Collection<Process> processes = new JsonSlurper().parse(processesJson.toCharArray()).collect {
            Process process = new Process(it)
            process.lastUpdate = LocalDateTime.now()
            return process
        }

        log.info "Saving parsed ${processes.size()} processes:\n${processes}"
        processRepository.save processes
    }
}
