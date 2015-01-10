package com.github.diplodoc.diplobase.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resource
import org.springframework.hateoas.hal.Jackson2HalModule
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class ProcessRunDataClient {

    private final static PROCESS_RUN = new ParameterizedTypeReference<Resource<ProcessRun>>() {}

    String rootUrl

    RestTemplate hateoasTemplate = newHateoasTemplate()

    ProcessRunDataClient(String rootUrl) {
        this.rootUrl = rootUrl
    }

    ProcessRun create(ProcessRun processRun) {
        ResponseEntity<Resource<ProcessRun>> response = hateoasTemplate.exchange("${rootUrl}/diplobase/processruns", HttpMethod.POST, new HttpEntity<ProcessRun>(processRun), PROCESS_RUN)
        fromResource(response.body)
    }

    private static ProcessRun fromResource(Resource<ProcessRun> resource) {
        ProcessRun processRun = resource.content
        String selfHref = resource.id.href
        processRun.id = Long.parseLong(selfHref.substring(selfHref.lastIndexOf('/') + 1))
        return processRun
    }

    private static RestTemplate newHateoasTemplate() {
        RestTemplate restTemplate = new RestTemplate()

        restTemplate.messageConverters.removeAll { messageConverter ->
            messageConverter instanceof MappingJackson2HttpMessageConverter
        }

        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.registerModule(new Jackson2HalModule())
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter()
        messageConverter.supportedMediaTypes = [ MediaTypes.HAL_JSON ]
        messageConverter.objectMapper = objectMapper

        restTemplate.messageConverters << messageConverter

        return restTemplate
    }
}
