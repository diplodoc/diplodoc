package com.github.diplodoc.diplocore.modules.data

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
import com.github.diplodoc.diplocore.modules.Bindable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author yaroslav.yermilov
 */
@Component('data.sources')
class Sources implements Bindable {

    @Autowired
    SourceRepository sourceRepository

    @Override
    void bindSelf(Binding binding) {
        binding.findAllSources = this.&findAll
    }

    Collection<Source> findAll() {
        sourceRepository.findAll()
    }
}