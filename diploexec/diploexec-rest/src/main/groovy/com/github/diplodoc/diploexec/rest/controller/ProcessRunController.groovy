package com.github.diplodoc.diploexec.rest.controller

import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diploexec.DiploexecRuntimeEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/api/v1')
class ProcessRunController {

    @Autowired
    DiploexecRuntimeEnvironment runtime

    @RequestMapping(value='/process/{id}/run', method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void run(@PathVariable('id') Long id, @RequestBody ProcessRun processRun) {
        assert false : 'not implemented yet'
    }
}
