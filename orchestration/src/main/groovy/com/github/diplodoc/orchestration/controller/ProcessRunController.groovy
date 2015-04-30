package com.github.diplodoc.orchestration.controller

import com.github.diplodoc.orchestration.Orchestrator
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
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
    Orchestrator orchestrator

    @RequestMapping(value='/process/{processId}/run', method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody String run(@PathVariable('processId') String processId) {
        orchestrator.run(new ObjectId(processId), []).toString()
    }
}
