class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{ }

        '/'(view: '/navigation/data')
        '/data'(view: '/navigation/data')
        '/orchestration'(view: '/navigation/orchestration')
        '/train-sets'(view: '/navigation/train-sets')

        '500'(view: '/error')
	}
}
