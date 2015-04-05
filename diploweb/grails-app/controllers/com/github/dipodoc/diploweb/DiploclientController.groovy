package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Post

class DiploclientController {

    def postList() {
        params.max = 20
        params.sort = 'publishTime'
        params.order = 'desc'
        respond Post.list(params), model: [ postInstanceCount: Post.count() ]
    }

    def postShow(Post postInstance) {
        respond postInstance
    }
}
