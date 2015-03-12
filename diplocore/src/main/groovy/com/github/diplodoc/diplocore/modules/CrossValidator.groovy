package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Topic
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.TopicRepository
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.RestTemplate

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

    RestTemplate restTemplate = new RestTemplate()

    @RequestMapping(value = '/', method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody String validate() {
        List postScores = []
        double collectionScore = 0
        long collectionTime = 0
        int collectionSize = 0

        Pageable pageable = new PageRequest(0, 5)

        while (true) {
            println "Loading page #${pageable.pageNumber}: from ${pageable.offset} to ${pageable.offset + pageable.pageSize - 1}..."

            Collection<Post> posts = postRepository.findAll(pageable).content
            pageable = pageable.next()

            if (!posts.isEmpty()) {
                postScores << posts .findAll { Post post ->
                                        println "Checking if [${post.id}] is in train set..."
                                        post.train_topics != null && !post.train_topics.isEmpty()
                                    }
                                    .collect { Post post ->
                                        println "[${post.id}] is in train set. Going to classify it..."

                                        long classificationStart = System.currentTimeMillis()
                                        restTemplate.getForObject("http://localhost:5000/post-type-classifier/post/${post.id}/classify", String)
                                        long classificationEnd = System.currentTimeMillis()
                                        collectionTime += (classificationEnd - classificationStart)

                                        println "[${post.id}] is classified. Going to score it's quality..."

                                        post = postRepository.findOne(post.id)

                                        double postScore = (post.predicted_topics
                                                .collect { prediction ->
                                                    Topic topic = topicRepository.findOne(prediction['topic_id'])
                                                    Double score = prediction['score']
                                                    post.train_topics.contains(topic) ? 1 - score : score
                                                }
                                                .sum()) / post.predicted_topics.size()
                                        collectionScore += postScore
                                        collectionSize++

                                        List predictedTopics = post.predicted_topics
                                                                        .sort{ prediction1, prediction2 ->
                                                                            Double.compare(prediction2['score'], prediction1['score'])
                                                                        }
                                                                        .collect{ prediction ->
                                                                            Topic topic = topicRepository.findOne(prediction['topic_id'])
                                                                            Double score = prediction['score']
                                                                            "${topic.label} : ${score}"
                                                                        }

                                        def dump = [
                                            'id': post.id,
                                            'url': post.url,
                                            'title': post.title,
                                            'train-topics': post.train_topics*.label,
                                            'predicted-topics': predictedTopics,
                                            'post-score': postScore,
                                            'classification-time': "${(classificationEnd - classificationStart)/1000}s"
                                        ]

                                        println ([ 'current_average_score': (collectionScore / collectionSize), 'current_average_time': "${(collectionTime / collectionSize)/1000}s", 'current_post': dump ].toMapString())
                                        FileUtils.writeStringToFile(new File("F:\\Temp\\post-scores\\post-${post.id}.score"), ([ 'current_average_score': (collectionScore / collectionSize), 'current_average_time': "${(collectionTime / collectionSize)/1000}s", 'current_post': dump ].toMapString()))

                                        return dump
                                    }
            } else {
                FileUtils.writeStringToFile(new File("F:\\Temp\\post-scores\\total.score"), ([ 'average_score': (collectionScore / collectionSize), 'average_time': "${(collectionTime / collectionSize)/1000}s", 'posts': postScores ].toMapString()))
                println ([ 'average_score': (collectionScore / collectionSize), 'average_time': "${(collectionTime / collectionSize)/1000}s", 'posts': postScores ].toMapString())
                return ([ 'average_score': (collectionScore / collectionSize), 'average_time': "${(collectionTime / collectionSize)/1000}s", 'posts': postScores ].toMapString())
            }
        }
    }
}
