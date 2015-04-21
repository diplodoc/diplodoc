class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{ }

        '/'(view: '/navigation/diplodata')
        '/diplodata'(view: '/navigation/diplodata')
        '/diploexec'(view: '/navigation/diploexec')
        '/train-sets'(view: '/navigation/train-sets')

        '/knu'(view: '/navigation/knu')

        '500'(view: '/error')
	}
}
