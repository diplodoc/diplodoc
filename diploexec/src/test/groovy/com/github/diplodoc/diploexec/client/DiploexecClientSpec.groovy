package com.github.diplodoc.diploexec.client

import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Process
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ProcessRun
import org.bson.types.ObjectId
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class DiploexecClientSpec extends Specification {

    DiploexecClient diploexecClient = new DiploexecClient()

    def 'void run(Process process, Map<String, Object> parameters)'() {
        given:
            Process process = new Process(id: new ObjectId('111111111111111111111111'))
            RestTemplate hateoasTemplate = Mock(RestTemplate)

        when:
            diploexecClient.hateoasTemplate = hateoasTemplate
            diploexecClient.run(process, [ 'key': 'value' ])

        then:
            1 * hateoasTemplate.exchange(
                { String it -> it.endsWith('/diploexec/process/run') },
                HttpMethod.POST,
                { HttpEntity<ProcessRun> it ->
                    it.body.processId == new ObjectId('111111111111111111111111') &&
                    it.body.parameters.size() == 1 &&
                    it.body.parameters[0].type == 'java.lang.String' &&
                    it.body.parameters[0].key == 'key' &&
                    it.body.parameters[0].value == '"value"'
                },
                new ParameterizedTypeReference<Resource<ProcessRun>>() {}
            )
    }
}
