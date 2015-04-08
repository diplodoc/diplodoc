package com.github.dipodoc.diploweb.domain.user

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId

@EqualsAndHashCode
class Role {

	static mapWith = 'mongo'

	ObjectId id


	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
