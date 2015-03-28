package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Post

class DiploclientController {

    def postList() {
        respond Post.list(max: 10, sort: 'publishTime', order: 'desc'), model:[postInstanceCount: Post.count()]
    }
}
