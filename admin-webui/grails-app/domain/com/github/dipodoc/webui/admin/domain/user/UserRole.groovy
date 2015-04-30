package com.github.dipodoc.webui.admin.domain.user

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class UserRole {

	static mapWith = 'mongo'

	ObjectId id


	ObjectId userId

	ObjectId roleId


	static constraints = {
		roleId validator: { ObjectId roleId, UserRole userRole ->
			if (userRole.userId == null) return
			boolean existing = false
			UserRole.withNewSession {
				existing = UserRole.exists(userRole.userId, roleId)
			}
			if (existing) {
				return 'userRole.exists'
			}
		}
	}


	static boolean exists(User user, Role role) {
		exists(user.id, role.id)
	}

	static boolean exists(ObjectId _userId, ObjectId _roleId) {
		UserRole.where {
			userId == _userId && roleId == _roleId
		}.count() > 0
	}

	static UserRole create(User user, Role role, boolean flush = false) {
		new UserRole(userId: user.id, roleId: role.id).save flush: flush, insert: true
	}
}
