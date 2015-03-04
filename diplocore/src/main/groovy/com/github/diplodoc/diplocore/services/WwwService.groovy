package com.github.diplodoc.diplocore.services

import com.github.diplodoc.diplobase.domain.mongodb.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
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
