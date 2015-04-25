<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Doc" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'doc.label', default: 'Doc')}" />
		<title><g:message message="socials" /></title>
	</head>

	<body>
		<a href="#list-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

		<div id="list-doc" class="content scaffold-list" role="main">
			<h1><g:message message="${docInstanceCount} socials" /></h1>

			<table>
				<tbody>
				<g:each in="${docInstanceList}" status="i" var="docInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
							<h3><g:link action="show" id="${docInstance.id}">${fieldValue(bean: docInstance, field: 'id')}</g:link></h3>

							<div><a href="${docInstance.uri}" target="_blank"><g:fieldValue bean="${docInstance}" field="uri"/></a> at ${fieldValue(bean: docInstance, field: 'loadTime')}</div>

							<div>${fieldValue(bean: docInstance, field: 'description')}</div>
						</td>
					</tr>
				</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${docInstanceCount ?: 0}" />
			</div>
		</div>

	</body>
</html>
