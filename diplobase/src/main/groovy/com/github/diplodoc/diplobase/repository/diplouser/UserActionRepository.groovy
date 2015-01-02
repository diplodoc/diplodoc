package com.github.diplodoc.diplobase.repository.diplouser

import com.github.diplodoc.diplobase.domain.diplouser.UserAction
import org.springframework.data.repository.CrudRepository

/**
 * @author yaroslav.yermilov
 */
interface UserActionRepository extends CrudRepository<UserAction, Long> {
}