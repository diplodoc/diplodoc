package com.github.dipodoc.diploweb.controller

import com.github.dipodoc.diploweb.domain.diplodata.Post
import org.springframework.security.access.annotation.Secured

@Secured([ 'ROLE_USER' ])
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
