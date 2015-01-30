package com.github.diplodoc.diplobase.shell

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SourcesCommandsSpec extends Specification {

    SourceRepository sourceRepository = Mock(SourceRepository)
    SourcesCommands sourcesCommands = new SourcesCommands(sourceRepository: sourceRepository)

    def '`sources list` command'() {
        when:
            sourceRepository.findAll() >> [
                new Source(id: 1, name: 'name-1', newPostsFinderModule: 'module-1'),
                new Source(id: 2, name: 'name-2', newPostsFinderModule: 'module-2')
            ]

        then:
            sourcesCommands.list() == '    1                        name-1                                          module-1\n' +
                                      '    2                        name-2                                          module-2'
    }

    def '`sources dump` command'() {
        when:
            sourceRepository.findOneByName('name') >> new Source(id: 1, name: 'name', newPostsFinderModule: 'module')

        then:
            String tempDir = File.createTempDir().absolutePath
            sourcesCommands.dump('name', "${tempDir}/file.txt")
            new File("${tempDir}/file.txt").text == '{"type":"com.github.diplodoc.diplobase.domain.diplodata.Source","id":1,"newPostsFinderModule":"module","name":"name"}'
    }
}
