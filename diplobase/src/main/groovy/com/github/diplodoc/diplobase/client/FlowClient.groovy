package com.github.diplodoc.diplobase.client

import com.github.diplodoc.diplobase.domain.diploexec.Flow
import groovy.json.JsonSlurper
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
class FlowClient {

    String rootUrl

    RestTemplate restTemplate = new RestTemplate()
    JsonSlurper jsonSlurper = new JsonSlurper()

    FlowClient(String rootUrl) {
        this.rootUrl = rootUrl
    }

    List<Flow> flows() {
        def flowsJson = jsonSlurper.parseText(restTemplate.getForObject("${rootUrl}/diplobase/flows", String))
        List flowsLinks = (flowsJson.links as List).findAll { link -> link.rel == 'flow' }

        flowsLinks.collect { flowLink ->
            jsonToFlow(jsonSlurper.parseText(restTemplate.getForObject(flowLink.href, String)))
        }
    }

    Flow findOneByName(String name) {
        def flowsJson = jsonSlurper.parseText(restTemplate.getForObject("${rootUrl}/diplobase/flows/search/findOneByName?name=${name}", String))
        def flowLink = (flowsJson.links as List).findAll { link -> link.rel == 'flow' }.first()

        jsonToFlow(jsonSlurper.parseText(restTemplate.getForObject(flowLink.href, String)))
    }

    private Flow jsonToFlow(def flowJson) {
        Long id = Long.parseLong(flowJson.'_links'.self.href.substring(flowJson.'_links'.self.href.lastIndexOf('/') + 1))
        String name = flowJson.name
        String definition = flowJson.definition

        new Flow(id: id, name: name, definition: definition)
    }
}
