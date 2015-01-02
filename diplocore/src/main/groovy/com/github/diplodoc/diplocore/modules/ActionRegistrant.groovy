package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplouser.UserAction
import com.github.diplodoc.diplobase.repository.diplouser.UserActionRepository
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class ActionRegistrant {

    @Autowired
    UserActionRepository userActionRepository

    def bind(Binding binding) {
        binding.register = this.&register
    }

    def register(UserAction userAction) {
        userActionRepository.save userAction
    }
}
