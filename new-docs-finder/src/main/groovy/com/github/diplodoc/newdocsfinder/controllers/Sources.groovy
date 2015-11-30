package com.github.diplodoc.newdocsfinder.controllers

import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * @author yaroslav.yermilov
 */
@RestController
class Sources {

    @Autowired
    SourceRepository sourceRepository

    @RequestMapping(value = '/sources/ids', method = RequestMethod.GET)
    List<String> all() {
        sourceRepository.findAll()*.id*.toString()
    }
}