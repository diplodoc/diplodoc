package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/post-dumper')
class PostDumper {

    @Autowired
    PostRepository postRepository

    @RequestMapping(value = '/post/{id}/dump', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void dumpPost(@PathVariable('id') String postId, @RequestBody String folder) {
        Post post = postRepository.findOne(postId)

        def postDump = []
        postDump << "${post.id}"
        postDump << "${post.url}"
        postDump << "${post.meaningText}"

        new File(folder, "post-${id}.dump").text = postDump.join('\n')
    }
}
