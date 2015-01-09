package com.github.diplodoc.diploexec.rest.controller

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diploexec.Diploexec
import org.springframework.beans.factory.annotation.Autowired
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
@RequestMapping('/api/v1')
class ProcessRunController {

    @Autowired
    Diploexec diploexec

    @RequestMapping(value='/process/run', method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody ProcessRun run(@RequestBody ProcessRun processRun) {
        
        assert false : 'not implemented yet'
    }
}
