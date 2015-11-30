package com.github.diplodoc.clientapi.controllers

import com.github.diplodoc.domain.mongodb.user.User
import com.github.diplodoc.domain.mongodb.data.Source
import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import com.github.diplodoc.clientapi.services.SecurityService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author yaroslav.yermilov
 */
@RestController
@Slf4j
class Sourcer {

    private final static Sort SORT = new Sort(Sort.Direction.ASC, 'name')
    private final static int DEFAULT_SIZE = 20

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    SecurityService securityService

    @RequestMapping(value = '/sources', method = RequestMethod.GET)
    List<Map<String, String>> sources(
                    @RequestParam(value = 'auth_provider', required = false) String authProvider,
                    @RequestParam(value = 'auth_type', required = false) String authType,
                    @RequestParam(value = 'auth_token', required = false) String authToken,
                    @RequestParam(value = 'page', required = false) Integer page,
                    @RequestParam(value = 'size', required = false) Integer size
            ) {

        User user = securityService.authenticate(authProvider, authType, authToken)

        if (!user) {
            return []
        }

        List<Source> sources = sourceRepository.findAll(new PageRequest(page?:0, size?:DEFAULT_SIZE, SORT)).content

        sources.collect { Source source ->
            [
                'name'   : source.name,
                'rssUrl' : source.rssUrl
            ]
        }
    }
}
