package com.github.diplodoc.diplocore.services

import com.github.diplodoc.diplobase.domain.diplodata.Post
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class Web {

    Document load(String url) {
        Jsoup.connect(url).get()
    }

    Document document(Post post) {
        Jsoup.parse(post.html)
    }
}
