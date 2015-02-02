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

    @CliCommand(value = 'sources dump', help = 'dump source to file')
    String dump(@CliOption(key = 'name', mandatory = true, help = 'source name') final String name,
                @CliOption(key = 'path', mandatory = true, help = 'path to dump file') final String path) {
        Source source = sourceRepository.findOneByName(name)

        Map<String, String> sourceProperties = source.properties.collectEntries { String key, Object value ->
            key.toString() != 'class' ? [ key, value ] : [ 'type', Source.class.name ]
        }
        new File(path).text = JsonOutput.toJson(sourceProperties)
    }
}
