package com.github.diplodoc.diplocore.modules.knu

import groovy.util.logging.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/local-file-loader')
@Slf4j
class LocalFileLoader {

    @RequestMapping(value = '/load', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def loadFiles(@PathVariable('path') path) {
        assert null : 'not implemeted yet'
    }
}
