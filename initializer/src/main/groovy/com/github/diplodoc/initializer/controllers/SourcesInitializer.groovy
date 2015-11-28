package com.github.diplodoc.initializer.controllers

import com.github.diplodoc.domain.mongodb.data.Source
import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * @author yaroslav.yermilov
 */
@RestController
@Slf4j
class SourcesInitializer {

    @Autowired
    SourceRepository sourceRepository

    @RequestMapping(value = '/sources/init', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void init() {
        log.info 'Going to delete all sources...'
        sourceRepository.deleteAll()

        log.info 'Reading init sources list...'
        String sourcesJson = getClass().getResource('/sources.json').text

        log.info "Parsing init sources list:\n${sourcesJson}"
        Collection<Source> sources = new JsonSlurper().parse(sourcesJson.toCharArray()).collect { new Source(it) }

        log.info "Saving parsed ${sources.size()} sources:\n${sources}"
        sourceRepository.save sources
    }
}
