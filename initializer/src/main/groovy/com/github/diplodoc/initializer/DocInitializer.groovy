package com.github.diplodoc.initializer;

import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

import java.util.Collection;

/**
 * @author yaroslav.yermilov
 */
@Controller
@Slf4j
public class DocInitializer {

    @Autowired
    DocRepository docRepository

    @RequestMapping(value = '/docs/clean', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void init() {
        log.info 'Going to delete all docs...'
        docRepository.deleteAll()
    }
}
