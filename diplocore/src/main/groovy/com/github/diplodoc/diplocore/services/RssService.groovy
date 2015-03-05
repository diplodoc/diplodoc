package com.github.diplodoc.diplocore.services

import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
class RssService {

    List<SyndEntry> feed(String url) {
        try {
            return new SyndFeedInput().build(new XmlReader(new URL(url))).entries
        } catch (e) {
            return []
        }
    }
}