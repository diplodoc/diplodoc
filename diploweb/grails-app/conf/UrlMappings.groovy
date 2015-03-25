class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{ }

        '/'(controller: 'post', action: 'list')

        '/diplodata'(controller: 'post', action: 'list')

        '500'(view: '/error')
	}
}
