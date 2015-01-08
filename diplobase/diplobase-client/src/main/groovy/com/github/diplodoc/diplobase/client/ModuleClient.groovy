package com.github.diplodoc.diplobase.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.diplodoc.diplobase.domain.diploexec.Module
import com.sun.org.apache.xpath.internal.operations.Mod
import groovy.json.JsonSlurper
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

/**
 * @author yaroslav.yermilov
 */
class ModuleClient {

    private final static MODULES = new ParameterizedTypeReference<Resources<Resource<Module>>>() {}
    private final static MODULE = new ParameterizedTypeReference<Resource<Module>>() {}

    String rootUrl

    RestTemplate hateoasTemplate = newHateoasTemplate()

    ModuleClient(String rootUrl) {
        this.rootUrl = rootUrl
    }

    List<Module> modules() {
        ResponseEntity<Resources<Resource<Module>>> response = hateoasTemplate.exchange("${rootUrl}/diplobase/modules", HttpMethod.GET, null, MODULES)
        response.body.collect() { Resource<Module> resource -> fromResource(resource) }
    }

    Module findOneByName(String name) {
        ResponseEntity<Resource<Module>> response = hateoasTemplate.exchange("${rootUrl}/diplobase/modules/search/findOneByName?name=${name}", HttpMethod.GET, null, MODULES)
        response.body.collect() { Resource<Module> resource -> fromResource(resource) }.first()
    }

    void update(Module module) {
        hateoasTemplate.exchange("${rootUrl}/diplobase/modules/${module.id}", HttpMethod.PUT, new HttpEntity<Module>(module), MODULE)
    }

    private static Module fromResource(Resource<Module> resource) {
        Module module = resource.content
        String selfHref = resource.id.href
        module.id = Long.parseLong(selfHref.substring(selfHref.lastIndexOf('/') + 1))
        return module
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
