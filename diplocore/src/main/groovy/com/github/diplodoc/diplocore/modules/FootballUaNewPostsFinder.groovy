package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Web
import groovy.util.logging.Slf4j
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('football.ua-new-posts-finder')
@Slf4j
class FootballUaNewPostsFinder implements Bindable {

    @Autowired
    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.findNewPosts = { Map params -> findNewPosts(params.source, params.action) }
    }

    void findNewPosts(Source source, Closure action) {
        log.info('looking for new posts from {}...', source.name)

        boolean newFound = true
        int archivePageIndex = 1

        while (newFound) {
            String archivePageUrl = "http://football.ua/newsarc/page${archivePageIndex}.html"
            log.debug('looking for archive page [{}]', archivePageUrl)

            Document archivePage = web.load(archivePageUrl)

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
                        log.debug('found new posts at {}', archivePageUrl)
                        newFound = true
                        action.call url
                    }
        }
        log.debug('all new posts have been found')
    }
}
