package com.github.dipodoc.diploweb.domain.user

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode(excludes = 'springSecurityService')
class User {

	static mapWith = 'mongo'

	ObjectId id


	String username
	String password
	boolean enabled = true
	boolean accountExpired = false
	boolean accountLocked = false
	boolean passwordExpired = false

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		version false

		password column: '`password`'
	}


	transient springSecurityService

	static transients = [ 'springSecurityService' ]

	Set<Role> getAuthorities() {
		UserRole.findAllByUserId(id).collect { Role.get(it.roleId) }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
