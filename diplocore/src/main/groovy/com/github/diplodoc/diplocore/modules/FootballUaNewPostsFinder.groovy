package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Web
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class FootballUaNewPostsFinder {

    Web web

    @Autowired
    PostRepository postRepository

    def bind(Binding binding) {
        binding.findNew = {
            Map params -> findNew(params.source, params.action)
        }
    }

    def findNew(def source, Closure action) {
        def newFound = true
        def archivePageIndex = 1

        while (newFound) {
            def archivePage = web.load("http://football.ua/newsarc/page${archivePageIndex}.html")

            def candidates = []
            archivePage.select('h4').select('a').each {
                candidates.add it.attr('href')
            }
            archivePageIndex++

            newFound = false
            candidates.each {
                url ->
                    if (doNotExistsWebPageFor(url)) {
                        newFound = true
                        action.call url
                    }
            }
        }
    }

    def doNotExistsWebPageFor(String url) {
        return postRepository.findOneByUrl(url) == null
    }
}
