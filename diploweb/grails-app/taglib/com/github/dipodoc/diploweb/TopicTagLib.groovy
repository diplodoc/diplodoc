package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Topic

class TopicTagLib {

    static String SPAN_OPEN = '<span class="property-value" aria-labelledby="train_topics-label">'
    static String SPAN_CLOSE = '</span>'

    static namespace = 'diplo'

    def topics = { attrs, body ->
        def topics = attrs['topics']

        topics = topics.collectMany(this.&unroll) as Set

        while (!topics.empty) {
            Topic leaf = topics.find { Topic leafCandidate ->
                topics.find({ Topic childCandidate -> childCandidate.parent == leafCandidate }) == null
            }

            Topic iter = leaf

            out << SPAN_OPEN

            while (iter != null) {
                topics.remove(iter)
                out << topicToHtml(iter)

                iter = iter.parent
                if (iter != null) {
                    out << ' <- '
                }
            }

            out << SPAN_CLOSE
        }
    }

    String topicToHtml(Topic topic) {
        g.link([ controller: 'topic', action: 'show', id: topic.id ], g.fieldValue([ bean: topic, field: 'label' ]))
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
