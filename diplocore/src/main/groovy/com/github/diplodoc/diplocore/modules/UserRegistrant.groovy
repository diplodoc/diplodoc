package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.diplouser.User
import com.github.diplodoc.diplobase.repository.diplouser.UserRepository
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author yaroslav.yermilov
 */
class UserRegistrant {

    @Autowired
    UserRepository userRepository

    def bind(Binding binding) {
        binding.register = this.&register
    }

    def register(User user) {
        userRepository.save user
    }
}
