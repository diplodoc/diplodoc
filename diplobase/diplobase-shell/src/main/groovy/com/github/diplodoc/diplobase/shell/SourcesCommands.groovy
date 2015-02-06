package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.client.diplodata.SourceDataClient
import com.github.diplodoc.diplobase.domain.diplodata.Source
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component
class SourcesCommands implements CommandMarker {

    @Autowired
    SourceDataClient sourceDataClient

    @CliCommand(value = 'sources list', help = 'list all sources')
    String list() {
        sourceDataClient.findAll().collect(SourcesCommands.&toSingleLineDescription).join('\n')
    }

    @CliCommand(value = 'sources update', help = 'update existing source')
    String update(@CliOption(key = '', mandatory = true, help = 'source name') final String name,
                  @CliOption(key = 'new-post-finder-module', mandatory = false, help = 'new posts finder module') final String newPostsFinderModule,
                  @CliOption(key = 'rss-url', mandatory = false, help = 'rss url') final String rssUrl) {
        Source source = sourceDataClient.findOneByName(name)
        source.newPostsFinderModule = newPostsFinderModule?:source.newPostsFinderModule
        source.rssUrl = rssUrl?:source.rssUrl

        source = sourceDataClient.save(source)
        toDescription(source)
    }

    @CliCommand(value = 'sources get', help = 'get source parameters')
    String get(@CliOption(key = '', mandatory = true, help = 'source name') final String name,
               @CliOption(key = 'representation', mandatory = false, help = 'convert to json', unspecifiedDefaultValue = 'text') final String representation) {
        Source source = sourceDataClient.findOneByName(name)

        switch (representation) {
            case 'text':
                toDescription(source)
            break

            case 'json':
                toJson(source)
            break

            default: 'Unknown presentation type'
        }
    }

    static String toSingleLineDescription(Source source) {
        "${source.id}".padLeft(5) +
        "${source.name}".padLeft(30)
    }

    static String toDescription(Source source) {
        'id:'.padRight(30) + "${source.id}\n" +
        'name:'.padRight(30) + "${source.name}\n" +
        'new posts finder module:'.padRight(30) + "${source.newPostsFinderModule}\n" +
        'rss url:'.padRight(30) + "${source.rssUrl}"
    }

    static String toJson(Source source) {
        Map<String, String> sourceProperties = source.properties.collectEntries { String key, Object value ->
            key.toString() != 'class' ? [ key, value ] : [ 'type', Source.class.name ]
        }
        JsonOutput.toJson(sourceProperties)
    }
}
