package com.github.dipodoc.diploweb.controller

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import org.springframework.security.access.annotation.Secured

@Secured([ 'ROLE_USER', 'ROLE_ADMIN' ])
class DiploclientController {

    def docList() {
        params.max = 20
        params.sort = 'publishTime'
        params.order = 'desc'
        respond Doc.list(params), model: [ docInstanceCount: Doc.count() ]
    }

    def docShow(Doc docInstance) {
        respond docInstance
    }
}
