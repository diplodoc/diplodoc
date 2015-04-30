package com.github.diplodoc.modules.data

import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import com.github.diplodoc.modules.services.AuditService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/data')
@Slf4j
class Sources {

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/sources', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody def all() {
        auditService.runMethodUnderAudit('data.Sources', 'all') { module, moduleMethod, moduleMethodRun ->
            def result = sourceRepository.findAll()*.id*.toString()
            def metrics = [ 'sources count': result.size() ]

            [ 'result': result, 'metrics': metrics ]
        }
    }
}