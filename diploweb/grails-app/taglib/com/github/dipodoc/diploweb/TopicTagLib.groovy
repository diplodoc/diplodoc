package com.github.dipodoc.diploweb

import com.github.dipodoc.diploweb.diplodata.Topic

class TopicTagLib {

    static String SPAN_OPEN = '<span class="property-value" aria-labelledby="train_topics-label">'
    static String SPAN_CLOSE = '</span>'

    // static defaultEncodeAs = [ taglib: 'html' ]

    static namespace = 'diplo'

    def topics = { attrs, body ->
        def topics = attrs['topics']

        topics = topics.collectMany(this.&unroll) as Set

        topics.each {
            out << SPAN_OPEN
            out << topicToHtml(it)
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
