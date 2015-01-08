package com.github.diplodoc.diplobase.client

import com.github.diplodoc.diplobase.domain.diploexec.Module
import com.sun.org.apache.xpath.internal.operations.Mod
import groovy.json.JsonSlurper
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class ModuleClient {

    String rootUrl

    RestTemplate restTemplate = new RestTemplate()
    JsonSlurper jsonSlurper = new JsonSlurper()

    ModuleClient(String rootUrl) {
        this.rootUrl = rootUrl
    }

    List<Module> modules() {
        def modulesJson = jsonSlurper.parseText(restTemplate.getForObject("${rootUrl}/diplobase/modules", String))
        List modulesLinks = (modulesJson.links as List).findAll { link -> link.rel == 'module' }

        modulesLinks.collect { moduleLink ->
            jsonToModule(jsonSlurper.parseText(restTemplate.getForObject(moduleLink.href, String)))
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
}
