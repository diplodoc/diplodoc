package com.github.diplodoc.diplocore.services

import com.github.diplodoc.diplobase.domain.diplodata.Post
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class Web {

    def load(String url) {
        Jsoup.connect(url).get()
    }

    def document(Post post) {
        Jsoup.parse(post.html)
    }
}
