package com.github.diplodoc.diplocore.modules.data

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Source
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.SourceRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
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
    ModuleRepository moduleRepository

    @Autowired
    ModuleMethodRepository moduleMethodRepository

    @Autowired
    ModuleMethodRunRepository moduleMethodRunRepository

    @RequestMapping(value = '/sources', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody Collection<String> all() {
        try {
            ModuleMethodRun moduleMethodRun = new ModuleMethodRun(startTime: LocalDateTime.now())

            log.debug "created moduleMethodRun: ${moduleMethodRun}"

            def result = sourceRepository.findAll()

            moduleMethodRun.endTime = LocalDateTime.now()
            moduleMethodRun.metrics = [ 'sources count': result.size() ]

            log.debug "updated moduleMethodRun: ${moduleMethodRun}"

            Module module = moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.data.Sources')
            log.debug "get module: ${module}"
            moduleMethodRun.moduleMethodId = moduleMethodRepository.findByName('all').find({ it.moduleId == module.id }).id
            log.debug "get moduleMethodId: ${moduleMethodRun.moduleMethodId}"
            moduleMethodRun = moduleMethodRunRepository.save moduleMethodRun
            log.debug "saved moduleMethodRun: ${moduleMethodRun}"

            return result*.id*.toString()
        } catch (e) {
            log.error 'failed', e
        }
    }
}