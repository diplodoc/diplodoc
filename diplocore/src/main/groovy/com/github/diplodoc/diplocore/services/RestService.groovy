package com.github.diplodoc.diplocore.services

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * @author yaroslav.yermilov
 */
@Service
class RestService {

    RestTemplate restTemplate = new RestTemplate()

    void get(Map params) {
        String url = params.url
        Class responseType = params.response?:String

        get(url, responseType)
    }

    def get(String url, Class responseType) {
       return restTemplate.getForObject(url, responseType)
    }
}
