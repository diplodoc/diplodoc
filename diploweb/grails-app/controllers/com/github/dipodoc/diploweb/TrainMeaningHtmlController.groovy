package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Post

class TrainMeaningHtmlController {

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def trainSet = Post.findAllByTrain_meaningHtmlIsNotNull(params)

        respond trainSet, model: [ postInstanceCount: Post.countByTrain_meaningHtmlIsNotNull() ]
    }
}
