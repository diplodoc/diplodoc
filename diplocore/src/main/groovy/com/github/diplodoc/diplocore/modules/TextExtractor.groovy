package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.Web
import groovy.util.logging.Slf4j
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('text-extractor')
@Slf4j
class TextExtractor implements Bindable {

    @Autowired
    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.extractText = { Map params -> extractText(params.from) }
    }

    Post extractText(Post post) {
        log.info('going to extract text from {}...', post.url)

        Map<Element, Double> divElements = [:]
        web.document(post).select('div').each { divElements[it] = 0.0 }

        decreaseByLinks divElements
        decreaseByChildren divElements
        increaseByPoints divElements
        increaseByTextSize divElements

        post = postRepository.findOne(post.id)
        post.meaningText = divElements.max { it.value }?.key?.text()?:web.document(post)
        post = postRepository.save post
        log.debug('for post {} meaning text extracted: {}', post.url, post.meaningText)
        return post
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
