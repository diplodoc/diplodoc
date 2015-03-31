package com.github.dipodoc.diploweb.train

import com.github.dipodoc.diploweb.diplodata.Post
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
class TrainTopicsController {

    static allowedMethods = [ save: 'PUT', saveAndNext: 'PUT', removeFromTrain: 'DELETE' ]

    Random random = new Random()

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def trainSet = Post.where { train_topics != null && train_topics.size() > 0 }

        respond trainSet.list(params), model: [ postInstanceCount: trainSet.count() ]
    }

    def trainNext() {
        int untrainedCount = Post.where({ train_topics == null || train_topics.isEmpty() }).count()
        int index = random.nextInt(untrainedCount)
        def params = [ offset: index, max: 1 ]

        Post randomUntrainedPost = Post.where({ train_topics == null || train_topics.isEmpty() }).find()
        [ postToTrain: randomUntrainedPost ]
    }

    def edit(Post postInstance) {
        respond postInstance
    }

    @Transactional
    def saveAndNext() {
        Post postToTrain = Post.get(params.id)
        //postToTrain.train_topics =

        if (postToTrain == null) {
            notFound()
            return
        }

        if (postToTrain.hasErrors()) {
            respond postToTrain.errors, view: 'trainNext'
            return
        }

        postToTrain.save flush:true

        redirect action: 'trainNext'
    }

    @Transactional
    def save() {
        Post postToTrain = Post.get(params.id)
        //postToTrain.train_topics =

        if (postToTrain == null) {
            notFound()
            return
        }

        if (postToTrain.hasErrors()) {
            respond postToTrain.errors, view: 'trainNext'
            return
        }

        postToTrain.save flush:true

        redirect action: 'list'
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

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'post.label', default: 'Post'), params.id])
                redirect action: 'list', method: 'GET'
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
