package com.github.diplodoc.diploexec.rest.webapi

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
class ProcessesController {

    @Autowired
    DiploexecRuntimeEnvironment runtime

    @RequestMapping(value='/process/{id}/start', method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void startProcess(@PathVariable('id') Long id, @RequestBody Map<String, Object> input) {
        runtime.startProcess(id, input)
    }
}
