package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Web
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('football.ua-new-posts-finder')
class FootballUaNewPostsFinder implements Bindable {

    @Autowired
    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.findNewPosts = {
            Map params -> findNewPosts(params.source, params.action)
        }
    }

    void findNewPosts(Source source, Closure action) {
        boolean newFound = true
        int archivePageIndex = 1

        while (newFound) {
            Document archivePage = web.load("http://football.ua/newsarc/page${archivePageIndex}.html")

            List<String> candidates = []
            archivePage.select('h4').select('a').each {
                candidates.add it.attr('href')
            }
            archivePageIndex++

            newFound = false
            candidates
                    .findAll { url ->
                        postRepository.findOneByUrl(url) == null
                    }.each { url ->
                        newFound = true
                        action.call url
                    }
        }
    }
}
