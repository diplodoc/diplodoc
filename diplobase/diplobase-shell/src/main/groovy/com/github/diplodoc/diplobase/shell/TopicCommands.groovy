package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.domain.mongodb.Topic
import com.github.diplodoc.diplobase.repository.mongodb.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

import java.util.regex.Pattern

/**
 * @author yaroslav.yermilov
 */
@Component
class TopicCommands implements CommandMarker {

    @Autowired
    TopicRepository topicRepository

    @CliCommand(value = 'topic load-dumps', help = 'load topic dumps')
    String loadDumps(@CliOption(key = '', mandatory = true, help = 'path') final String path) {
        new File(path, 'topics.dump').readLines().findAll { !it.empty }.collect { String line ->
            String label = line.split(Pattern.quote('<-'))[0]

            Topic topic, parent

            if (line.split(Pattern.quote('<-')).size() > 1) {
                String parentLabel = line.split(Pattern.quote('<-'))[1]
                if (!topicRepository.findOneByLabel(parentLabel)) {
                    parent = new Topic(label: parentLabel)
                    parent = topicRepository.save parent
                } else {
                    parent = topicRepository.findOneByLabel parentLabel
                }
            } else {
                parent = null
            }

            if (!topicRepository.findOneByLabel(label)) {
                topic = new Topic(label: label, parent: parent)
            } else {
                topic = topicRepository.findOneByLabel label
                topic.parent = parent
            }

            topicRepository.save topic

            "${topic.label} -> ${topic.parent?.label}"
        }.join('\n')
    }
}
