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
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class ModuleClient {

    private final static MODULES = new ParameterizedTypeReference<Resources<Resource<Module>>>() {}

    String rootUrl

    RestTemplate hateoasTemplate = newHateoasTemplate()

    ModuleClient(String rootUrl) {
        this.rootUrl = rootUrl
    }

    List<Module> modules() {
        ResponseEntity<Resources<Resource<Module>>> response = hateoasTemplate.exchange("${rootUrl}/diplobase/modules", HttpMethod.GET, null, MODULES)
        response.body.collect { Resource<Module> resource ->
            Module module = resource.content
            String selfHref = resource.id.href
            module.id = Long.parseLong(selfHref.substring(selfHref.lastIndexOf('/') + 1))
            return module
        }
    }

    Module findOneByName(String name) {
        def modulesJson = jsonSlurper.parseText(restTemplate.getForObject("${rootUrl}/diplobase/modules/search/findOneByName?name=${name}", String))
        def moduleLink = (modulesJson.links as List).findAll { link -> link.rel == 'module' }.first()

        jsonToModule(jsonSlurper.parseText(restTemplate.getForObject(moduleLink.href, String)))
    }

    Module save(Module module) {
        restTemplate.put("${rootUrl}/diplobase/modules/${module.id}", module)

        def modulesJson = jsonSlurper.parseText(restTemplate.getForObject("${rootUrl}/diplobase/modules/${module.id}", String))
        def moduleLink = (modulesJson.links as List).findAll { link -> link.rel == 'module' }.first()

        jsonToModule(jsonSlurper.parseText(restTemplate.getForObject(moduleLink.href, String)))
    }

    private Module jsonToModule(def moduleJson) {
        Long id = Long.parseLong(moduleJson.'_links'.self.href.substring(moduleJson.'_links'.self.href.lastIndexOf('/') + 1))
        String name = moduleJson.name
        String definition = moduleJson.definition

        new Module(id: id, name: name, definition: definition)
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
