class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{ }

        '/'(view: '/navigation/diplodata')
        '/diplodata'(view: '/navigation/diplodata')
        '/diploexec'(view: '/navigation/diploexec')

        '500'(view: '/error')
	}
}
