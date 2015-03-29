package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Post

class TrainMeaningHtmlController {

    Random random = new Random()

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def trainSet = Post.findAllByTrain_meaningHtmlIsNotNull(params)

        respond trainSet, model: [ postInstanceCount: Post.countByTrain_meaningHtmlIsNotNull() ]
    }

    def trainNext() {
        int index = random.nextInt(Post.count())
        def params = [ offset: index, max: 1 ]

        def postToTrain = Post.findByTrain_meaningHtmlIsNull(params)
        respond postToTrain
    }
}
