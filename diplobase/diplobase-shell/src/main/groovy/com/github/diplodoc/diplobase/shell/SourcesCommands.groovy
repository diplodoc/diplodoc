package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
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
    SourceRepository sourceRepository

    @CliCommand(value = 'sources list', help = 'list all sources')
    String list() {
        sourceRepository.findAll().collect { Source source ->
            "${source.id}".padLeft(5) +
            "${source.name}".padLeft(30) +
            "${source.newPostsFinderModule}".padLeft(50)
        }.join('\n')
    }

    @CliCommand(value = 'sources get', help = 'get source parameters')
    String get(@CliOption(key = '', mandatory = true, help = 'source name') final String name,
               @CliOption(key = 'representation', mandatory = false, help = 'convert to json', unspecifiedDefaultValue = 'text') final String representation) {
        Source source = sourceRepository.findOneByName(name)

        switch (representation) {
            case 'text':
                'id:'.padRight(30) + "${source.id}\n" +
                'name:'.padRight(30) + "${source.name}\n" +
                'new posts finder module:'.padRight(30) + "${source.newPostsFinderModule}"
            break

            case 'json':
                Map<String, String> sourceProperties = source.properties.collectEntries { String key, Object value ->
                    key.toString() != 'class' ? [ key, value ] : [ 'type', Source.class.name ]
                }
                JsonOutput.toJson(sourceProperties)
            break

            default: 'Unknown presentation type'
        }
    }
}
