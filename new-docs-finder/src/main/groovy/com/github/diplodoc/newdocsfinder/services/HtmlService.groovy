package com.github.diplodoc.newdocsfinder.services

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class HtmlService {

    Document load(String url) {
        Jsoup.connect(url).get()
    }

    Document parse(String html) {
        Jsoup.parse(html)
    }

    Element parseFragment(String html) {
        Jsoup.parseBodyFragment(html).body().child(0)
    }
}
