package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.mongodb.Topic
import com.github.diplodoc.diplobase.repository.mongodb.TopicRepository
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class TopicCommandsSpec extends Specification {

    TopicRepository topicRepository = Mock(TopicRepository)

    TopicCommands topicCommands = new TopicCommands(topicRepository: topicRepository)

    def 'Topic loadDump(String dump) - non-existent parent, non-existent label'() {
        when:
            topicRepository.findOneByLabel('parent') >> null
            topicRepository.findOneByLabel('label') >> null

            Topic actual = topicCommands.loadDump('label <- parent')

        then:
            1 * topicRepository.save(new Topic(label: 'parent')) >> { it[0] }
            1 * topicRepository.save(new Topic(label: 'label', parent: new Topic(label: 'parent'))) >> { it[0] }

        expect:
            actual == new Topic(label: 'label', parent: new Topic(label: 'parent'))
    }

    def 'Topic loadDump(String dump) - existent parent, non-existent label'() {
        when:
            topicRepository.findOneByLabel('parent') >> new Topic(label: 'parent')
            topicRepository.findOneByLabel('label') >> null

            Topic actual = topicCommands.loadDump('label <- parent')

        then:
            1 * topicRepository.save(new Topic(label: 'label', parent: new Topic(label: 'parent'))) >> { it[0] }

        expect:
            actual == new Topic(label: 'label', parent: new Topic(label: 'parent'))
    }

    def 'Topic loadDump(String dump) - no parent, non-existent label'() {
        when:
            topicRepository.findOneByLabel('label') >> null

            Topic actual = topicCommands.loadDump('label')

        then:
            1 * topicRepository.save(new Topic(label: 'label')) >> { it[0] }

        expect:
            actual == new Topic(label: 'label')
    }

    def 'Topic loadDump(String dump) - non-existent parent, existent label'() {
        when:
            topicRepository.findOneByLabel('parent') >> null
            topicRepository.findOneByLabel('label') >> new Topic(label: 'label', parent: new Topic(label: 'old-parent'))

            Topic actual = topicCommands.loadDump('label <- parent')

        then:
            1 * topicRepository.save(new Topic(label: 'parent')) >> { it[0] }
            1 * topicRepository.save(new Topic(label: 'label', parent: new Topic(label: 'parent'))) >> { it[0] }

        expect:
            actual == new Topic(label: 'label', parent: new Topic(label: 'parent'))
    }

    def 'Topic loadDump(String dump) - existent parent, existent label'() {
        when:
            topicRepository.findOneByLabel('parent') >> new Topic(label: 'parent')
            topicRepository.findOneByLabel('label') >> new Topic(label: 'label', parent: new Topic(label: 'old-parent'))

            Topic actual = topicCommands.loadDump('label <- parent')

        then:
            1 * topicRepository.save(new Topic(label: 'label', parent: new Topic(label: 'parent'))) >> { it[0] }

        expect:
            actual == new Topic(label: 'label', parent: new Topic(label: 'parent'))
    }

    def 'Topic loadDump(String dump) - no parent, existent label'() {
        when:
            topicRepository.findOneByLabel('label') >> new Topic(label: 'label', parent: new Topic(label: 'old-parent'))

            Topic actual = topicCommands.loadDump('label')

        then:
            1 * topicRepository.save(new Topic(label: 'label')) >> { it[0] }

        expect:
            actual == new Topic(label: 'label')
    }

    def 'static toSingleLineDescription(Topic topic) - topic with parent'() {
        when:
            Topic topic = new Topic(label: 'label', parent: new Topic(label: 'parent'))

        then:
            String actual = TopicCommands.toSingleLineDescription(topic)

        expect:
            actual == 'label -> parent'
    }

    def 'static toSingleLineDescription(Topic topic) - topic with no parent'() {
        when:
            Topic topic = new Topic(label: 'label')

        then:
            String actual = TopicCommands.toSingleLineDescription(topic)

        expect:
            actual == 'label'
    }
}
