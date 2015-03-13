package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Topic
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.TopicRepository
import com.github.diplodoc.diplocore.services.RestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/cross-validator')
class CrossValidator {

    @Autowired
    PostRepository postRepository

    @Autowired
    TopicRepository topicRepository

    @Autowired
    ResourceLoader resourceLoader

    @Autowired
    RestService restService

    @RequestMapping(value = '/', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody String validate() {
        List postsDumps = []
        double collectionScore = 0
        long collectionClassificationTime = 0
        int collectionSize = 0

        Pageable pageable = new PageRequest(0, 5)
        int totalPages = postRepository.findAll(pageable).totalPages
        int totalElements = postRepository.findAll(pageable).totalElements

        long validationStart = System.currentTimeMillis()

        totalPages.times {
            println "Loading page #${pageable.pageNumber + 1} (of ${totalPages}): from ${pageable.offset + 1} to ${pageable.offset + pageable.pageSize} (of ${totalElements})..."

            Collection<Post> validationPosts = postRepository.findAll(pageable).content.findAll(this.&isCrossValidation)

            validationPosts.each { Post post ->
                collectionSize++
                println "[${post.id}] is #${collectionSize} in train set. Starting its validation..."

                def classificationResult = classify(post)
                post = classificationResult.post
                collectionClassificationTime += classificationResult.time

                double postScore = calculateScore(post)
                collectionScore += postScore

                List predictedTopics = getPredictedTopics(post)

                Map postsDump = getPostDump(post, predictedTopics, postScore, classificationResult.time)
                log(post, postsDump, collectionSize, collectionScore, collectionClassificationTime)
                postsDumps << postsDump
            }

            LocalDateTime willBeDone = getWillBeDoneAt(pageable.pageNumber, totalPages, validationStart)
            println "Predicting to be ready at ${willBeDone}"

            pageable = pageable.next()
        }

        def validationResult = [ 'average_score': (collectionScore / collectionSize), 'average_time': "${(collectionClassificationTime / collectionSize)/1000}s", 'posts': postsDumps ].toMapString()

        resourceLoader.getResource('file://F:\\Temp\\post-scores').createRelative('total.score').file.withOutputStream {
            it.write(validationResult.bytes)
        }

        return validationResult
    }

    boolean isCrossValidation(Post post) {
        post.train_topics != null && !post.train_topics.isEmpty()
    }

    LocalDateTime getWillBeDoneAt(int pageNumber, int totalPages, long validationStart) {
        long now = System.currentTimeMillis()
        long pageDone = pageNumber + 1
        long pageLeft = totalPages - pageNumber - 1
        double needMoreTime = 1.0 * (now - validationStart) / pageDone * pageLeft

        LocalDateTime.now().plus(Math.round(needMoreTime), ChronoUnit.MILLIS)
    }

    def classify(Post post) {
        long classificationStart = System.currentTimeMillis()
        restService.get(url: "http://localhost:5000/post-type-classifier/post/${post.id}/classify")
        long classificationEnd = System.currentTimeMillis()
        post = postRepository.findOne(post.id)

        return [ post: post, time: (classificationEnd - classificationStart) ]
    }

    double calculateScore(Post post) {
        (post.predicted_topics
                .collect { prediction ->
                    Topic topic = topicRepository.findOne(prediction['topic_id'])
                    Double score = prediction['score']
                    unrollTopics(post.train_topics).contains(topic) ? 1 - score : score
                }
                .sum()) / post.predicted_topics.size()
    }

    List getPredictedTopics(Post post) {
        post.predicted_topics
                .sort{ prediction1, prediction2 ->
                    Double.compare(prediction2['score'], prediction1['score'])
                }
                .collect{ prediction ->
                    Topic topic = topicRepository.findOne(prediction['topic_id'])
                    Double score = prediction['score']
                    "${topic.label}: ${score}"
                }
    }

    Map getPostDump(Post post, List predictedTopics, double postScore, double classificationTime) {
        [
            'id': post.id,
            'url': post.url,
            'title': post.title,
            'train-topics': unrollTopics(post.train_topics)*.label,
            'predicted-topics': predictedTopics,
            'post-score': postScore,
            'classification-time': "${classificationTime/1000}s"
        ]
    }

    Collection<Topic> unrollTopics(Collection<Topic> original) {
        Set<Topic> result = new HashSet<>()

        original.each {
            result.add(it)
            while (it.parent != null) {
                result.add(it.parent)
                it = it.parent
            }
        }

        return result
    }

    void log(Post post, Map postDump, int collectionSize, double collectionScore, long collectionClassificationTime) {
        Map logInfo = [
            'current_average_score': (collectionScore / collectionSize),
            'current_average_time': "${(collectionClassificationTime / collectionSize)/1000}s",
            'current_post': postDump
        ]

        println logInfo.toMapString()
        resourceLoader.getResource('file://F:\\Temp\\post-scores\\').createRelative("post-${post.id}.score").file.withOutputStream {
            it.write(logInfo.toMapString().bytes)
        }
    }
}
