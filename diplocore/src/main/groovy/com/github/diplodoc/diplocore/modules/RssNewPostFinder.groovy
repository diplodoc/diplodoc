package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.Rss
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author yaroslav.yermilov
 */
@Component('rss-new-posts-finder')
@Slf4j
class RssNewPostFinder implements Bindable {

    @Autowired
    PostRepository postRepository

    @Autowired
    Rss rss

    @Override
    void bindSelf(Binding binding) {
        binding.findNewPosts = { Map params -> findNewPosts(params.source, params.action) }
    }

    void findNewPosts(Source source, Closure action) {
        log.info('looking for new posts from {}...', source.name)

        rss.feed(source.rssUrl).each { rssEntry ->
            log.debug('found rss entry {}', rssEntry)

            if (!postRepository.findOneByUrl(rssEntry.link)) {
                Post post = new Post(   url: rssEntry.link,
                                        source: source,
                                        title: rssEntry.title,
                                        description: rssEntry.description.value,
                                        publishTime: LocalDateTime.ofInstant(rssEntry.publishedDate.toInstant(), ZoneId.systemDefault())
                )
                log.debug('found new post {}', post)

                action.call post
            }
        }
    }
}
