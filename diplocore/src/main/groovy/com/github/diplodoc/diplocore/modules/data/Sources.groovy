package com.github.diplodoc.diplocore.modules.data

import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.SourceRepository
import com.github.diplodoc.diplocore.modules.Bindable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/data')
class Sources {

    @Autowired
    SourceRepository sourceRepository

    @RequestMapping(value = '/sources', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody Collection<Source> all() {
        sourceRepository.findAll()
    }
}