package com.github.diplodoc.diplocore.services

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class WwwService {

    Document load(String url) {
        Jsoup.connect(url).get()
    }

    Document parse(String html) {
        Jsoup.parse(html)
    }
}
