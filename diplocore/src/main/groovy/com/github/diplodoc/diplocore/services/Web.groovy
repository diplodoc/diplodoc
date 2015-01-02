package com.github.diplodoc.diplocore.services

import com.github.diplodoc.diplobase.domain.diplodata.Post
import org.jsoup.Jsoup

/**
 * @author yaroslav.yermilov
 */
class Web {

    def load(String url) {
        Jsoup.connect(url).get()
    }

    def document(Post post) {
        Jsoup.parse(post.html)
    }
}
