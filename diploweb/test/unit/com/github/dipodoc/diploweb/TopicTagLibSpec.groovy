package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Topic
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.bson.types.ObjectId
import spock.lang.Specification

@TestFor(TopicTagLib)
@Mock(Topic)
class TopicTagLibSpec extends Specification {

    def 'single topic with parent'() {
        given:
            Topic topic1 = new Topic(id: new ObjectId('111111111111111111111111'), label: 'label-1')
            Topic topic2 = new Topic(id: new ObjectId('222222222222222222222222'), label: 'label-2', parent: topic1)

        when:
            def result = applyTemplate'<diplo:topics topics="${topic}" />', [ topic: topic2 ]

        then:
            result == '<div><a href="/topic/show/222222222222222222222222">label-2</a> <- <a href="/topic/show/111111111111111111111111">label-1</a></div>'
    }

    def 'single topic with no parent'() {
        given:
            Topic topic = new Topic(id: new ObjectId('111111111111111111111111'), label: 'label')

        when:
            def result = applyTemplate'<diplo:topics topics="${topic}" />', [ topic: topic ]

        then:
            result == '<div><a href="/topic/show/111111111111111111111111">label</a></div>'
    }

    def 'topics list with single hierarchy'() {
        given:
            Topic topic1 = new Topic(id: new ObjectId('111111111111111111111111'), label: 'label-1')
            Topic topic2 = new Topic(id: new ObjectId('222222222222222222222222'), label: 'label-2', parent: topic1)
            Topic topic3 = new Topic(id: new ObjectId('333333333333333333333333'), label: 'label-3', parent: topic2)

        when:
            def result = applyTemplate'<diplo:topics topics="${topics}" />', [ topics: [ topic1, topic3 ] ]

        then:
            result == '<div><a href="/topic/show/333333333333333333333333">label-3</a> <- <a href="/topic/show/222222222222222222222222">label-2</a> <- <a href="/topic/show/111111111111111111111111">label-1</a></div>'
    }

    def 'topics map'() {
        given:
            Topic topic1 = new Topic(label: 'label-1').save flush: true
            Topic topic2 = new Topic(label: 'label-2', parent: topic1).save flush: true

            def topicsMap = [ [ 'topic_id': topic1.id, 'score': 0.1234567 ], [ 'topic_id': topic2.id, 'score': 0.9876543 ] ]

        when:
            def result = applyTemplate'<diplo:topics topics="${topics}" />', [ topics: topicsMap ]

        then:
            result == """<div><a href="/topic/show/$topic2.id">label-2</a> (0,988) <- <a href="/topic/show/$topic1.id">label-1</a> (0,123)</div>"""
    }

    def 'topics map with all hierarchy'() {
        given:
            Topic topic1 = new Topic(label: 'label-1').save flush: true
            Topic topic2 = new Topic(label: 'label-2', parent: topic1).save flush: true

            def topicsMap = [ [ 'topic_id': topic1.id, 'score': 0.1234567 ], [ 'topic_id': topic2.id, 'score': 0.9876543 ] ]

        when:
            def result = applyTemplate'<diplo:topics topics="${topics}" hierarchy="all" />', [ topics: topicsMap ]

        then:
            result == """<div><a href="/topic/show/$topic2.id">label-2</a> (0,988) <- <a href="/topic/show/$topic1.id">label-1</a> (0,123)</div><div><a href="/topic/show/$topic1.id">label-1</a> (0,123)</div>"""
    }

    def 'single topic with no parent with div class'() {
        given:
            Topic topic = new Topic(id: new ObjectId('111111111111111111111111'), label: 'label')

        when:
            def result = applyTemplate'<diplo:topics topics="${topic}" divClass="some class" />', [ topic: topic ]

        then:
            result == '<div class="some class"><a href="/topic/show/111111111111111111111111">label</a></div>'
    }
}
