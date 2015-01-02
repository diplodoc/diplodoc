package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplodata.Source
import com.github.diplodoc.diplobase.repository.diplodata.SourceRepository
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class DiplobaseSource {

    @Autowired
    SourceRepository sourceRepository

    def bind(Binding binding) {
        binding.load = this.&load
    }

    def load(Source source) {
        sourceRepository.findOneByName(source.name)
    }
}
