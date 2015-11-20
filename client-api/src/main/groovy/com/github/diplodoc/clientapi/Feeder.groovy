package com.github.diplodoc.clientapi

import com.github.diplodoc.domain.mongodb.User
import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import com.github.diplodoc.services.AuditService
import com.github.diplodoc.services.SecurityService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@Slf4j
class Feeder {

    private final static Sort SORT = new Sort(Sort.Direction.DESC, 'publishTime')
    private final static int DEFAULT_SIZE = 20

    @Autowired
    DocRepository docRepository

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    AuditService auditService

    @Autowired
    SecurityService securityService

    @RequestMapping(value = '/feed', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody def feed( @RequestParam(value = 'auth_provider', required = false) String authProvider,
                            @RequestParam(value = 'auth_type', required = false) String authType,
                            @RequestParam(value = 'auth_token', required = false) String authToken,
                            @RequestParam(value = 'page', required = false) Integer page,
                            @RequestParam(value = 'size', required = false) Integer size) {

        User user = securityService.authenticate(authProvider, authType, authToken)

        if (!user) {
            return []
        }

        auditService.runMethodUnderAudit('client.Feeder', 'feed') { module, moduleMethod, moduleMethodRun ->
            moduleMethodRun.parameters = [ 'page': page, 'size': size, 'userId': user.id.toString() ]

            List<Doc> docs = docRepository.findAll(new PageRequest(page?:0, size?:DEFAULT_SIZE, SORT)).content

            def feed = docs.collect { Doc doc ->
                [   'id'         : doc.id.toString(),
                    'url'        : doc.uri,
                    'title'      : doc.title,
                    'time'       : doc.publishTime,
                    'sourceName' : sourceRepository.findOne(doc.sourceId).name
                ]
            }

            [ 'result': feed, 'moduleMethodRun': moduleMethodRun ]
        }
    }
}
