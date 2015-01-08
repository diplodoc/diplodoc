package com.github.diplodoc.diplobase.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.diplodoc.diplobase.domain.diploexec.Process
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.hal.Jackson2HalModule
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
class ProcessDataClient {

    private final static PROCESSES = new ParameterizedTypeReference<Resources<Resource<Process>>>() {}
    private final static PROCESS = new ParameterizedTypeReference<Resource<Process>>() {}

    String rootUrl

    RestTemplate hateoasTemplate = newHateoasTemplate()

    ProcessDataClient(String rootUrl) {
        this.rootUrl = rootUrl
    }

    List<Process> processes() {
        ResponseEntity<Resources<Resource<Process>>> response = hateoasTemplate.exchange("${rootUrl}/diplobase/processes", HttpMethod.GET, null, PROCESSES)
        response.body.collect ProcessDataClient.&fromResource
    }

    Process findOneByName(String name) {
        ResponseEntity<Resource<Process>> response = hateoasTemplate.exchange("${rootUrl}/diplobase/processes/search/findOneByName?name=${name}", HttpMethod.GET, null, PROCESSES)
        response.body.collect(ProcessDataClient.&fromResource).first()
    }

    void update(Process process) {
        process.lastUpdate = LocalDateTime.now().toString()
        hateoasTemplate.exchange("${rootUrl}/diplobase/processes/${process.id}", HttpMethod.PUT, new HttpEntity<Process>(process), PROCESS)
    }

    void delete(Process process) {
        hateoasTemplate.exchange("${rootUrl}/diplobase/processes/${process.id}", HttpMethod.DELETE, null, PROCESS)
    }

    private static Process fromResource(Resource<Process> resource) {
        Process process = resource.content
        String selfHref = resource.id.href
        process.id = Long.parseLong(selfHref.substring(selfHref.lastIndexOf('/') + 1))
        return process
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
