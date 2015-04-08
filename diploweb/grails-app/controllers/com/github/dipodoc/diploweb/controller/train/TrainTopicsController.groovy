package com.github.dipodoc.diploweb.controller.train

import com.github.dipodoc.diploweb.domain.diplodata.Post
import com.github.dipodoc.diploweb.domain.diplodata.Topic
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
class TrainTopicsController {

    static allowedMethods = [ save: 'PUT', saveAndNext: 'PUT', addTopicToTrainingSet: 'PUT', removeFromTrain: 'DELETE', removeTopicFromTrainingSet: 'DELETE' ]

    Random random = new Random()

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def trainSet = Post.where { train_topics != null && train_topics.size() > 0 }

        respond trainSet.list(params), model: [ postInstanceCount: trainSet.count() ]
    }

    def trainNext() {
        if (params.id == null) {
            int untrainedCount = Post.where({ train_topics == null || train_topics.isEmpty() }).count()
            int index = random.nextInt(untrainedCount)
            def params = [ offset: index, max: 1 ]

            Post randomUntrainedPost = Post.where({ train_topics == null || train_topics.isEmpty() }).list(params)[0]
            [ postToTrain: randomUntrainedPost ]
        } else {
            [ postToTrain: Post.get(params.id) ]
        }
    }

    def edit(Post postInstance) {
        respond postInstance
    }

    @Transactional
    def removeFromTrain() {
        Post postInstance = Post.get(params.id)

        if (postInstance == null) {
            notFound()
            return
        }

        postInstance.train_topics = []
        postInstance.save flush:true

        redirect action: 'list'
    }

    @Transactional
    def removeTopicFromTrainingSet() {
        Post postInstance = Post.get(params.postId)
        Topic topicInstance = Topic.get(params.topicId)

        if (postInstance == null || topicInstance == null) {
            notFound()
            return
        }

        postInstance.train_topics.remove(topicInstance)
        postInstance.save flush:true

        redirect action: params.redirectTo, id: postInstance.id, params: [ id: postInstance.id ]
    }

    @Transactional
    def addTopicToTrainingSet() {
        Post postInstance = Post.get(params.postId)
        Topic topicInstance = Topic.get(params.topicId)

        if (postInstance == null || topicInstance == null) {
            notFound()
            return
        }

        postInstance.train_topics.add(topicInstance)
        postInstance.save flush:true

        redirect action: params.redirectTo, id: postInstance.id, params: [ id: postInstance.id ]
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [ message(code: 'post.label', default: 'Post'), params.id ])
                redirect action: 'list', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
