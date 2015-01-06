package com.github.diplodoc.diploexec.webapi

import com.github.diplodoc.diploexec.DiploexecRuntimeEnvironment
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
@RequestMapping('/api/v1')
class StatusController {

    @Autowired
    DiploexecRuntimeEnvironment runtime

    @RequestMapping(value='/status', method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody def status() {
        runtime.status()
    }
}
