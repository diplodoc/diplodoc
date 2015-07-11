package com.github.diplodoc.orchestration.controller

import com.github.diplodoc.domain.mongodb.orchestration.Process
import com.github.diplodoc.domain.mongodb.orchestration.ProcessRun
import com.github.diplodoc.domain.repository.mongodb.orchestration.ProcessRepository
import com.github.diplodoc.orchestration.Orchestrator
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
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
class OrchestratorController {

    @Autowired
    Orchestrator orchestrator

    @Autowired
    ProcessRepository processRepository

    @RequestMapping(value='/process/{processId}/run', method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody String run(@PathVariable('processId') String processId) {
        Process process = processRepository.findOne new ObjectId(processId)
        ProcessRun processRun = orchestrator.start process

        return processRun.id
    }
}
