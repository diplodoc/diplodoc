class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{ }

        '/'(view: 'diplodata')
        '/diplodata'(view: 'diplodata')

        '500'(view: '/error')
	}
}
