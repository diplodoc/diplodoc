package com.github.diplodoc.clientapi.controllers

import com.github.diplodoc.clientapi.services.SecurityService
import com.github.diplodoc.domain.mongodb.user.User
import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
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
class Feeder {

    private final static Sort SORT = new Sort(Sort.Direction.DESC, 'publishTime')
    private final static int DEFAULT_SIZE = 20

    @Autowired
    DocRepository docRepository

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    SecurityService securityService

    @RequestMapping(value = '/feed', method = RequestMethod.GET)
    List<Map<String, String>> feed(
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

        List<Doc> docs = docRepository.findBySourceIdIn(user.interestedInSourcesIds, new PageRequest(page?:0, size?:DEFAULT_SIZE, SORT))

        docs.collect { Doc doc ->
                [
                    'id'         : doc.id.toString(),
                    'url'        : doc.uri,
                    'title'      : doc.title,
                    'time'       : doc.publishTime,
                    'sourceName' : sourceRepository.findOne(doc.sourceId).name
                ]
            }
    }
}
