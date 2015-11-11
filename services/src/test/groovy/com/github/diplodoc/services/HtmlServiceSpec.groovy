package com.github.diplodoc.services

import com.github.diplodoc.services.HtmlService
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class HtmlServiceSpec extends Specification {

    HtmlService htmlService = new HtmlService()

    def 'Document parse(String html)'() {
        when:
            def actual = htmlService.parse('<html><head></head><body><div></div><div></div></body></html>')

        then:
            actual.children().size() == 1
            actual.html() == '<html>\n <head></head>\n <body>\n  <div></div>\n  <div></div>\n </body>\n</html>'
            actual.outerHtml() == '<html>\n <head></head>\n <body>\n  <div></div>\n  <div></div>\n </body>\n</html>'
    }

    def 'Element parseFragment(String html)'() {
        when:
            def actual = htmlService.parseFragment('<div><div></div></div>')

        then:
            actual.children().size() == 1
            actual.html() == '<div></div>'
            actual.outerHtml() == '<div>\n <div></div>\n</div>'
    }
}
