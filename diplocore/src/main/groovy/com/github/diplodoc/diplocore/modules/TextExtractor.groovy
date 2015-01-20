package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Post
import com.github.diplodoc.diplobase.repository.diplodata.PostRepository
import com.github.diplodoc.diplocore.services.Web
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class TextExtractor implements Bindable {

    Web web

    @Autowired
    PostRepository postRepository

    @Override
    void bindSelf(Binding binding) {
        binding.extractText = {
            Map params -> extractText(params.from)
        }
    }

    def extractText(Post post) {
        def divElements = [:]
        web.document(post).select('div').each {
            divElements[it] = 0.0
        }

        decreaseByLinks divElements
        decreaseByChildren divElements
        increaseByPoints divElements
        increaseByTextSize divElements

        post.meaningText = divElements.max {
            it.value
        }.key.text()

        post = postRepository.findOne(post.id)
        postRepository.save post
    }

    def decreaseByLinks(Map<Element, Double> divElements) {
        divElements.each {
            double size = it.key.html().size()
            double linksCount = it.key.select('a').size()

            if (size > 0) {
                it.value = it.value - linksCount / Math.sqrt(size)
            }
        }
    }

    def decreaseByChildren(Map<Element, Double> divElements) {
        divElements.each {
            double size = it.key.html().size()
            double childrenCount = it.key.allElements.size()

            if (size > 0) {
                it.value = it.value - childrenCount / Math.sqrt(size)
            }
        }
    }

    def increaseByPoints(Map<Element, Double> divElements) {
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

    def increaseByTextSize(Map<Element, Double> divElements) {
        divElements.each {
            double size = it.key.html().size()
            double textSize = it.key.text().size()

            if (size > 0) {
                it.value = it.value + textSize / Math.sqrt(size)
            }
        }
    }
}
