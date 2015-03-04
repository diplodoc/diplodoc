package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.WwwService
import groovy.util.logging.Slf4j
import org.jsoup.nodes.Element
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
@RequestMapping('/text-extractor')
@Slf4j
class TextExtractor {

    @Autowired
    WwwService wwwService

    @Autowired
    PostRepository postRepository

    @RequestMapping(value = '/post/{id}/extract-text', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void extractText(@PathVariable('id') String postId) {
        Post post = postRepository.findOne postId

        log.info('going to extract text from {}...', post.url)

        Map<Element, Double> divElements = [:]
        wwwService.parse(post.html).select('div').each { divElements[it] = 0.0 }

        decreaseByLinks divElements
        decreaseByChildren divElements
        increaseByPoints divElements
        increaseByTextSize divElements

        post.meaningText = divElements.max { it.value }?.key?.text()?:wwwService.parse(post.html)
        post = postRepository.save post

        log.debug('for post {} meaning text extracted: {}', post.url, post.meaningText)
    }

    void decreaseByLinks(Map<Element, Double> divElements) {
        divElements.each {
            double size = it.key.html().size()
            double linksCount = it.key.select('a').size()

            if (size > 0) {
                it.value = it.value - linksCount / Math.sqrt(size)
            }
        }
    }

    void decreaseByChildren(Map<Element, Double> divElements) {
        divElements.each {
            double size = it.key.html().size()
            double childrenCount = it.key.allElements.size()

            if (size > 0) {
                it.value = it.value - childrenCount / Math.sqrt(size)
            }
        }
    }

    void increaseByPoints(Map<Element, Double> divElements) {
        divElements.each {
            double size = it.key.html().size()
            double pointsCount = 0;

            '.,;!?'.each {
                point -> pointsCount += it.key.text().count(point)
            }

            if (size > 0) {
                it.value = it.value + pointsCount / Math.sqrt(size)
            }
        }
    }

    void increaseByTextSize(Map<Element, Double> divElements) {
        divElements.each {
            double size = it.key.html().size()
            double textSize = it.key.text().size()

            if (size > 0) {
                it.value = it.value + textSize / Math.sqrt(size)
            }
        }
    }
}
