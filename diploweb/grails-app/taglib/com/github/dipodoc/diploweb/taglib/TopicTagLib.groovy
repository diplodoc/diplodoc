package com.github.dipodoc.diploweb.taglib

import com.github.dipodoc.diploweb.domain.diplodata.Topic

class TopicTagLib {

    static def NIL = new Object()

    static namespace = 'diplo'

    def topics = { attrs, body ->
        def topicsAttr = attrs['topics']
        String hierarchy = attrs['hierarchy']?:'single'
        String divClass = attrs['divClass']
        Integer maxTopicsCount = Integer.parseInt(attrs['maxTopicsCount']?:'0')

        Map topicsMap = [:]

        if (topicsAttr instanceof Topic) {
            unroll(topicsAttr).each { Topic topic -> topicsMap[topic] = NIL }
        } else if (topicsAttr[0] instanceof Topic) {
            topicsAttr.collectMany(this.&unroll).each { Topic topic -> topicsMap[topic] = NIL }
        } else {
            Collection allTopics = Topic.list()

            topicsAttr.each { def topic ->
                Topic topicInstance = allTopics.find({ it.id == topic['topic_id'] })
                unroll(topicInstance).each { Topic unrolledTopic ->
                    topicsMap[unrolledTopic] = topicsMap[unrolledTopic]?:0
                }
                topicsMap[topicInstance] = topic['score']
            }
        }

        Map originalMap = new HashMap(topicsMap)
        int topicsCount = 0

        while ((!topicsMap.isEmpty()) && (maxTopicsCount == 0 || topicsCount < maxTopicsCount)) {
            def leaf
            if (hierarchy == 'single') {
                leaf = topicsMap    .findAll { Topic leafCandidate, def leafScore ->
                                        topicsMap.find({ Topic childCandidate, def childScore -> childCandidate.parent == leafCandidate }) == null
                                    }
                                    .max { topic -> topic.value }
            } else {
                leaf = topicsMap.max { topic -> topic.value }
            }

            Topic root = leaf.key
            Topic iter = root

            if (divClass) {
                out << """<div class="${divClass}">"""
            } else {
                out << '<div>'
            }

            while (iter != null) {
                def score = originalMap[iter]
                out << topicRepresentation(iter, score)

                if (hierarchy == 'single') {
                    topicsMap.remove(iter)
                }

                iter = iter.parent
                if (iter != null) {
                    out << ' <- '
                }
            }
            topicsMap.remove(root)

            out << '</div>'

            topicsCount++
        }
    }

    String topicRepresentation(Topic topic, def score) {
        String representation = g.link([ controller: 'topic', action: 'show', id: topic.id ], g.fieldValue([ bean: topic, field: 'label' ]))
        if (score != null && score != NIL) {
            representation += " (${String.format('%.3f', score as double)})"
        }
        return representation
    }

    Collection unroll(Topic topic) {
        Collection result = [ topic ]
        while (topic.parent != null) {
            result << topic.parent
            topic = topic.parent
        }

        return result
    }
}
