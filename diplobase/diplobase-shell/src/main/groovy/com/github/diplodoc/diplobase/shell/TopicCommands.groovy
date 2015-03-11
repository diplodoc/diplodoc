package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.mongodb.Topic
import com.github.diplodoc.diplobase.repository.mongodb.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
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

    @Autowired
    ResourceLoader resourceLoader

    @CliCommand(value = 'topic load-dumps', help = 'load topic dumps')
    String loadDumps(@CliOption(key = '', mandatory = true, help = 'path to dump file') final String path) {
        resourceLoader.getResource(path).file.readLines().findAll { !it.empty }.collect { String line ->
            Topic topic = loadDump(line)
            toSingleLineDescription(topic)
        }.join('\n')
    }

    Topic loadDump(String dump) {
        String[] labels = dump.split(Pattern.quote('<-'))

        Topic topic, parent

        if (labels.size() > 1) {
            String parentLabel = labels[1].trim()
            if (!topicRepository.findOneByLabel(parentLabel)) {
                parent = new Topic(label: parentLabel)
                parent = topicRepository.save parent
            } else {
                parent = topicRepository.findOneByLabel parentLabel
            }
        } else {
            parent = null
        }

        String label = labels[0].trim()

        if (!topicRepository.findOneByLabel(label)) {
            topic = new Topic(label: label, parent: parent)
        } else {
            topic = topicRepository.findOneByLabel label
            topic.parent = parent
        }
        topicRepository.save topic

        return topic
    }

    static toSingleLineDescription(Topic topic) {
        if (topic.parent) {
            "${topic.label} -> ${topic.parent.label}"
        } else {
            "${topic.label}"
        }
    }
}
