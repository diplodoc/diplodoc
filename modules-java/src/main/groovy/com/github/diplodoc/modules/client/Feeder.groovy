package com.github.diplodoc.modules.client

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.modules.services.AuditService
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
@RequestMapping('/client/feeder')
@Slf4j
class Feeder {

    private final static Sort SORT = new Sort(Sort.Direction.DESC, 'publishTime')
    private final static int DEFAULT_SIZE = 20

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/feed', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody def feed(@RequestParam(value = 'page', required = false) Integer page, @RequestParam(value = 'size', required = false) Integer size) {
        auditService.runMethodUnderAudit('client.Feeder', 'feed') { module, moduleMethod, moduleMethodRun ->
            moduleMethodRun.parameters = [ 'page': page, 'size': size ]

            List<Doc> docs = docRepository.findByKnuIsNull(new PageRequest(page?:0, size?:DEFAULT_SIZE, SORT))

            def feed = docs.collect { Doc doc ->
                [   'id'         : doc.id.toString(),
                    'url'        : doc.uri,
                    'title'      : doc.title,
                    'time'       : doc.publishTime,
                    'description': doc.description
                ]
            }

            [ 'result': feed, 'moduleMethodRun': moduleMethodRun ]
        }
    }
}
