package com.github.dipodoc.webui.admin.domain.user

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Role {

	static mapWith = 'mongo'

	ObjectId id


	String authority

	static mapping = {
		version false
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
