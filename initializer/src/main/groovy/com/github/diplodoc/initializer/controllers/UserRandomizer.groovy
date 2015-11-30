package com.github.diplodoc.initializer.controllers

import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import com.github.diplodoc.domain.repository.mongodb.user.UserRepository
import groovy.util.logging.Slf4j
import org.apache.commons.lang.math.RandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * @author yaroslav.yermilov
 */
@RestController
@Slf4j
class UserRandomizer {

    @Autowired
    UserRepository userRepository

    @Autowired
    SourceRepository sourceRepository

    @RequestMapping(value = '/user/interested-in-sources/randomize', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void init() {
        log.info 'Going to randomize sources, in which users are interested...'

        List<String> sourcesIds = sourceRepository.findAll()*.id
        log.info "All sources we have: $sourcesIds"

        userRepository.findAll().each { user ->
            Collections.shuffle(sourcesIds)
            user.interestedInSourcesIds = sourcesIds[0..RandomUtils.nextInt(sourcesIds.size())]
            log.info "User with id [$user.id] is randomly interested in following sources [$user.interestedInSourcesIds]"

            userRepository.save user
            log.info "User with id [$user.id] successfuly saved"
        }

        log.info 'Randomizing finished successfuly'
    }
}
