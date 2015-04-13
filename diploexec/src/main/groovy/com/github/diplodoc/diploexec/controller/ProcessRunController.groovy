package com.github.diplodoc.diploexec.controller

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRunParameter
import com.github.diplodoc.diploexec.Diploexec
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
class ProcessRunController {

    @Autowired
    @Lazy
    Diploexec diploexec

    @RequestMapping(value='/process/{processId}/run', method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody String run(@PathVariable('processId') String processId) {
        diploexec.run(new ObjectId(processId), []).toString()
    }
}
