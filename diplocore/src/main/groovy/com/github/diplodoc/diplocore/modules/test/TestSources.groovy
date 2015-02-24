package com.github.diplodoc.diplocore.modules.test

import com.github.diplodoc.diplobase.domain.mongodb.Source
import com.github.diplodoc.diplobase.repository.mongodb.SourceRepository
import com.github.diplodoc.diplocore.modules.Bindable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('test.data.sources')
class TestSources implements Bindable {

    @Autowired
    SourceRepository sourceRepository

    @Override
    void bindSelf(Binding binding) {
        binding.findAllSources = this.&findAll
    }

    Collection<Source> findAll() {
        [
            new Source(id: '0', name: 'name-0', newPostsFinderModule: 'newPostsFinderModule-0', rssUrl: 'rssUrl-0'),
            new Source(id: '1', name: 'name-1', newPostsFinderModule: 'newPostsFinderModule-1', rssUrl: 'rssUrl-1')
        ]
    }
}