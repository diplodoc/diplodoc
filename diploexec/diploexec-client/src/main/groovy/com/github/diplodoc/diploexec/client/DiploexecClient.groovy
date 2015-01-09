package com.github.diplodoc.diploexec.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.diplodoc.diplobase.domain.diploexec.Process
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRun
import com.github.diplodoc.diplobase.domain.diploexec.ProcessRunParameter
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resource
import org.springframework.hateoas.hal.Jackson2HalModule
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class DiploexecClient {

    private final static PROCESS_RUN = new ParameterizedTypeReference<Resource<ProcessRun>>() {}

    String rootUrl

    RestTemplate hateoasTemplate = newHateoasTemplate()

    DiploexecClient(String rootUrl) {
        this.rootUrl = rootUrl
    }

    ProcessRun run(Process process, Map<String, Object> parameters) {
        ProcessRun processRun = new ProcessRun()
        processRun.process = process
        processRun.parameters = parameters.collect { String key, Object value ->
            new ProcessRunParameter(key: key, value: value, type: value.class.name, processRun: processRun)
        }

        ResponseEntity<Resource<ProcessRun>> response = hateoasTemplate.exchange("${rootUrl}/diploexec/api/v1/process/run", HttpMethod.POST, processRun, PROCESS_RUN)
        fromResource(response)
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
