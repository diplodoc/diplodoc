package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Post

class DiploclientController {

    def postList(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Post.list(params), model:[postInstanceCount: Post.count()]
    }
}
