package com.github.diplodoc.diploexec.rest.controller

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRunParameter
import com.github.diplodoc.diploexec.Diploexec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/api/v1')
class ProcessRunController {

    @Autowired
    @Lazy
    Diploexec diploexec

    @RequestMapping(value='/process/run', method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody Resource<ProcessRun> run(@RequestBody ProcessRun processRun) {
        ProcessRun result = diploexec.run(processRun)

        Resource<ProcessRun> resource = new Resource<>(result)
        resource.add(ControllerLinkBuilder.linkTo(ProcessRunController).slash('process').slash('run').withSelfRel())
        return resource
    }

    @RequestMapping(value='', method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody ResourceSupport links() {
        ResourceSupport resource = new ResourceSupport()
        resource.add(ControllerLinkBuilder.linkTo(ProcessRunController).slash('process').slash('run').withRel('run'))
        return resource
    }
}
