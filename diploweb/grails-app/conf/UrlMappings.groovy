class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{ }

        '/'(view: 'diplodata')
        '/diplodata'(view: 'diplodata')
        '/diploexec'(view: 'diploexec')

        '500'(view: '/error')
	}
}
