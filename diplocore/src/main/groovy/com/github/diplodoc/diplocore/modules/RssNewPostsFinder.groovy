package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.SourceRepository
import com.github.diplodoc.diplocore.services.RssService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/rss-new-posts-finder')
@Slf4j
class RssNewPostsFinder {

    @Autowired
    PostRepository postRepository

    @Autowired
    SourceRepository sourceRepository

    @Autowired
    RssService rssService

    @RequestMapping(value = '/source/{id}/new-posts', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody List<String> newPosts(@PathVariable('id') String sourceId) {
        Source source = sourceRepository.findOne sourceId

        log.info('looking for new posts from {}...', source.name)

        List<Post> posts = rssService
                                .feed(source.rssUrl)
                                .findAll { rssEntry -> !postRepository.findOneByUrl(rssEntry.link) }
                                .collect { rssEntry ->
                                    new Post(   url: rssEntry.link,
                                                source: source,
                                                title: rssEntry.title,
                                                description: rssEntry.description.value,
                                                publishTime: LocalDateTime.ofInstant(rssEntry.publishedDate.toInstant(), ZoneId.systemDefault())
                                    )
                                }

        postRepository.save posts

        return posts*.id
    }
}
