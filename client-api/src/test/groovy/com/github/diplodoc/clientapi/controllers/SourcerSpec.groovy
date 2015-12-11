package com.github.diplodoc.clientapi.controllers

import com.github.diplodoc.clientapi.services.SecurityService
import com.github.diplodoc.domain.mongodb.data.Source
import com.github.diplodoc.domain.mongodb.user.User
import com.github.diplodoc.domain.repository.mongodb.data.SourceRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author yaroslav.yermilov
 */
class SourcerSpec extends Specification {

    private final static Sort SORT = new Sort(Sort.Direction.ASC, 'name')

    SecurityService securityService = Mock(SecurityService)
    SourceRepository sourceRepository = Mock(SourceRepository)

    Sourcer sourcer = new Sourcer(securityService: securityService, sourceRepository: sourceRepository)

    def mockMvc = MockMvcBuilders.standaloneSetup(sourcer).build()


    def 'GET /sources - default page and size parameters'() {
        setup:
            User user = new User(interestedInSourcesIds: [ 'id-1' ])
            1 * securityService.authenticate('auth_provider', 'auth_type', 'auth_token') >> user

            Source source1 = new Source(id: 'id-1', name: 'source-1', rssUrl: 'rss-url-1')
            Source source2 = new Source(id: 'id-2', name: 'source-2', rssUrl: 'rss-url-2')
            1 * sourceRepository.findAll(new PageRequest(0, 20, SORT)) >> new PageImpl<Source>([ source1, source2 ])

        when:
            def response = mockMvc.perform(get('/sources?auth_provider=auth_provider&auth_type=auth_type&auth_token=auth_token').contentType(APPLICATION_JSON_UTF8))

        then:
            response
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$', hasSize(2)))
                .andExpect(jsonPath('$[0].name', is('source-1')))
                .andExpect(jsonPath('$[0].interested', is(true)))
                .andExpect(jsonPath('$[0].rssUrl', is('rss-url-1')))
                .andExpect(jsonPath('$[1].name', is('source-2')))
                .andExpect(jsonPath('$[1].interested', is(false)))
                .andExpect(jsonPath('$[1].rssUrl', is('rss-url-2')))
    }

    def 'GET /sources - with page and size parameters'() {
        setup:
            User user = new User(interestedInSourcesIds: [ 'id-1' ])
            1 * securityService.authenticate('auth_provider', 'auth_type', 'auth_token') >> user

            Source source1 = new Source(id: 'id-1', name: 'source-1', rssUrl: 'rss-url-1')
            Source source2 = new Source(id: 'id-2', name: 'source-2', rssUrl: 'rss-url-2')
            1 * sourceRepository.findAll(new PageRequest(2, 28, SORT)) >> new PageImpl<Source>([ source1, source2 ])

        when:
            def response = mockMvc.perform(get('/sources?auth_provider=auth_provider&auth_type=auth_type&auth_token=auth_token&page=2&size=28').contentType(APPLICATION_JSON_UTF8))

        then:
            response
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$', hasSize(2)))
                .andExpect(jsonPath('$[0].name', is('source-1')))
                .andExpect(jsonPath('$[0].interested', is(true)))
                .andExpect(jsonPath('$[0].rssUrl', is('rss-url-1')))
                .andExpect(jsonPath('$[1].name', is('source-2')))
                .andExpect(jsonPath('$[1].interested', is(false)))
                .andExpect(jsonPath('$[1].rssUrl', is('rss-url-2')))
    }
    def 'GET /sources - user is not found'() {
        setup:
            1 * securityService.authenticate(_, _, _) >> null

        when:
            def response = mockMvc.perform(get('/sources?auth_provider=auth_provider&auth_type=auth_type&auth_token=auth_token').contentType(APPLICATION_JSON_UTF8))

        then:
            response
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$', hasSize(0)))
    }
}
