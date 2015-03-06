package com.github.diplodoc.diplocore.modules

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
@RequestMapping('/tags-deducer')
class TagsDeducer {

    @RequestMapping(value = '/post/{id}/deduce', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void deduce(@PathVariable('id') String postId) {
        assert null : 'not implemented yet'
    }
}
