package com.github.diplodoc.diplocore.modules.data

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.SourceRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.AuditService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/diplodata')
@Slf4j
class Sources {

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/sources', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody def all() {
        auditService.runMethodUnderAudit('com.github.diplodoc.diplocore.modules.data.Sources', 'all') { module, moduleMethod, moduleMethodRun ->
            def result = sourceRepository.findAll()*.id*.toString()
            def metrics = [ 'sources count': result.size() ]

            [ 'result': result, 'metrics': metrics ]
        }
    }
}