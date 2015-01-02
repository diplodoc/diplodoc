package com.github.diplodoc.diplobase.repository.diplouser

import com.github.diplodoc.diplobase.domain.diplouser.User
import org.springframework.data.repository.CrudRepository

/**
 * @author yaroslav.yermilov
 */
interface UserRepository extends CrudRepository<User, Long> {
}