package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Topic
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class CrossValidatorSpec extends Specification {

    CrossValidator crossValidator = Spy(new CrossValidator())

    def 'Collection<Topic> unrollTopics(Collection<Topic> original)'() {
        setup:
            Topic topic1 = new Topic(label: 'topic-1')
            Topic topic2 = new Topic(label: 'topic-2')
            Topic topic3 = new Topic(label: 'topic-3')
            Topic topic4 = new Topic(label: 'topic-4')
            topic2.parent = topic1
            topic3.parent = topic2

        when:
            Collection<Topic> actual = crossValidator.unrollTopics([ topic2, topic3, topic4 ])

        then:
            actual == [ topic1, topic2, topic3, topic4 ] as Set
    }
}
