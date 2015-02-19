package com.github.diplodoc.diplocore.services

import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class Rss {

    List<SyndEntry> feed(String url) {
        new SyndFeedInput().build(new XmlReader(new URL(url))).entries
    }
}
