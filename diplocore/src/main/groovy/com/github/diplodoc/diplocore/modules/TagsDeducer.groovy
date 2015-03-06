package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.WwwService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/tags-deducer')
class TagsDeducer {

    @Autowired
    PostRepository postRepository

    @Autowired
    WwwService wwwService

    @RequestMapping(value = '/post/{id}/deduce', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void deduceTags(@PathVariable('id') String postId) {
        Post post = postRepository.findOne(postId)

        if (post.source.name.contains('pravda.com.ua')) {
            deduceTagsFromPravdaComUa(post)
        }
        if (post.source.name.contains('football.ua')) {
            deduceTagsFromFootbalUa(post)
        }
        if (post.source.name.contains('habrahabr.ru')) {
            deduceTagsFromHabrahabrRu(post)
        }
        if (post.source.name.contains('geektimes.ru')) {
            deduceTagsFromGeektimeRu(post)
        }

        postRepository.save(post)
    }

    private void deduceTagsFromPravdaComUa(Post post) {
        post.tags = [:]
        post.tags['pravda.com.ua'] = 1.0
        post.tags['новости'] = 1.0

        wwwService.parse(post.html).select('meta[name="keywords"]').attr('content').split(',').each { String tag ->
            post.tags[tag.trim()] = 1.0
        }
    }

    private void deduceTagsFromFootbalUa(Post post) {

    }

    private void deduceTagsFromHabrahabrRu(Post post) {

    }

    private void deduceTagsFromGeektimeRu(Post post) {

    }
}
